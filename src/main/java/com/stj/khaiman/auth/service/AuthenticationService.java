package com.stj.khaiman.auth.service;

import com.stj.khaiman.auth.constants.JwtConstant;
import com.stj.khaiman.auth.dto.request.LoginRequest;
import com.stj.khaiman.auth.dto.request.RegisterRequest;
import com.stj.khaiman.auth.dto.response.LoginResponse;
import com.stj.khaiman.auth.dto.response.RegisterResponse;
import com.stj.khaiman.auth.entity.BlackListToken;
import com.stj.khaiman.auth.entity.RefreshToken;
import com.stj.khaiman.auth.exception.AuthenticationErrorCode;
import com.stj.khaiman.auth.mapper.RefreshTokenMapper;
import com.stj.khaiman.auth.record.IssuedToken;
import com.stj.khaiman.auth.repository.BlackListTokenRepository;
import com.stj.khaiman.auth.repository.RefreshTokenRepository;
import com.stj.khaiman.common.exception.AppErrorCode;
import com.stj.khaiman.common.exception.AppException;
import com.stj.khaiman.user.entity.RoleEntity;
import com.stj.khaiman.user.entity.UserEntity;
import com.stj.khaiman.user.enums.UserStatus;
import com.stj.khaiman.user.enums.UserType;
import com.stj.khaiman.user.mapper.UserMapper;
import com.stj.khaiman.user.repository.RoleRepository;
import com.stj.khaiman.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final BlackListTokenRepository blackListTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenMapper refreshTokenMapper;


    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            throw new AppException(AuthenticationErrorCode.USER_EXISTED);
        }

        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new AppException(AppErrorCode.INVALID_INPUT);
        }

        RoleEntity customerRole = roleRepository.findByRoleName(UserType.CUSTOMER.name())
                .orElseThrow(() -> new IllegalStateException("Role CUSTOMER has not been initialized"));

        UserEntity user = userMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setUserStatus(UserStatus.ACTIVE);
        user.addRole(customerRole);

        return userMapper.toRegisterResponse(userRepository.save(user));
    }

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        Authentication authenticated;
        try {
            authenticated = authenticationManager.authenticate(authenticationToken);

        } catch (LockedException e) {
            throw new AppException(AuthenticationErrorCode.ACCOUNT_BANNED);
        } catch (DisabledException e) {
            throw new AppException(AuthenticationErrorCode.ACCOUNT_INACTIVE);
        } catch (BadCredentialsException e) {
            throw new AppException(AuthenticationErrorCode.UNAUTHORIZED);
        }

        UserEntity user = (UserEntity) authenticated.getPrincipal();

        //new login = new session
        return issueTokens(user, UUID.randomUUID().toString());
    }

    @Transactional
    public String logout(Jwt jwt) {
        refreshTokenRepository.findBySessionId(jwt.getClaimAsString(JwtConstant.SESSION_ID))
                .ifPresent(refreshTokenRepository::delete);

        blackListTokenRepository.save(BlackListToken.builder()
                .jti(jwt.getId())
                .expiresAt(jwt.getExpiresAt())
                .tokenType(jwt.getClaimAsString(JwtConstant.TOKEN_TYPE))
                .userId(Long.parseLong(jwt.getSubject()))
                .build());
        return "Logout success!";
    }

    @Transactional
    public LoginResponse refreshToken(String token) {
        //check token
        Jwt jwt = jwtService.verifyToken(token);

        //delete old token in one step: 0 rows = token already used (or by a concurrent request)
        //if a check below throws, @Transactional rolls this delete back
        if (refreshTokenRepository.deleteByJti(jwt.getId()) == 0) {
            throw new AppException(AuthenticationErrorCode.INVALID_TOKEN);
        }

        //get user with that token
        UserEntity user = userRepository.findWithRolesById(Long.parseLong(jwt.getSubject()))
                .orElseThrow(() -> new AppException(AuthenticationErrorCode.INVALID_TOKEN));

        if (user.getUserStatus() == UserStatus.BANNED) {
            throw new AppException(AuthenticationErrorCode.ACCOUNT_BANNED);
        }
        if (user.getUserStatus() == UserStatus.INACTIVE) {
            throw new AppException(AuthenticationErrorCode.ACCOUNT_INACTIVE);
        }

        //keep the session id of the current login session
        return issueTokens(user, jwt.getClaimAsString(JwtConstant.SESSION_ID));
    }

    private LoginResponse issueTokens(UserEntity user, String sessionId) {
        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        List<String> roleNames = user.getRoleNames();

        String accessToken = jwtService.generateAccessToken(user.getId(), roleNames, authorities, sessionId);
        IssuedToken issuedToken = jwtService.generateRefreshToken(user.getId(), sessionId);

        RefreshToken refreshToken = refreshTokenMapper.toRefreshTokenEntity(issuedToken);
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(issuedToken.value())
                .build();
    }
}
