package com.stj.khaiman.auth.service;

import com.stj.khaiman.auth.dto.request.LoginRequest;
import com.stj.khaiman.auth.dto.request.RegisterRequest;
import com.stj.khaiman.auth.dto.response.LoginResponse;
import com.stj.khaiman.auth.dto.response.RegisterResponse;
import com.stj.khaiman.auth.exception.AuthenticationErrorCode;
import com.stj.khaiman.common.exception.AppException;
import com.stj.khaiman.user.entity.UserEntity;
import com.stj.khaiman.user.enums.UserStatus;
import com.stj.khaiman.user.mapper.UserMapper;
import com.stj.khaiman.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            throw new AppException(AuthenticationErrorCode.USER_EXISTED);
        }
        UserEntity user = userMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserStatus(UserStatus.ACTIVE);

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
        String role = "demo";

        String accessToken = jwtService.generateAccessToken(user.getId(), role);
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(role)
                .build();
    }
}
