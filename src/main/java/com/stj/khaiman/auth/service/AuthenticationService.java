package com.stj.khaiman.auth.service;

import com.stj.khaiman.auth.dto.request.LoginRequest;
import com.stj.khaiman.auth.dto.request.RegisterRequest;
import com.stj.khaiman.auth.dto.response.LoginResponse;
import com.stj.khaiman.auth.dto.response.RegisterResponse;
import com.stj.khaiman.common.exception.AppErrorCode;
import com.stj.khaiman.common.exception.AppException;
import com.stj.khaiman.common.exception.ErrorCode;
import com.stj.khaiman.user.entity.UserEntity;
import com.stj.khaiman.user.enums.UserStatus;
import com.stj.khaiman.user.mapper.UserMapper;
import com.stj.khaiman.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public RegisterResponse register(RegisterRequest request){
        if(userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            throw new AppException(AppErrorCode.USER_EXISTED);
        }
        UserEntity user = userMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserStatus(UserStatus.ACTIVE);

        return userMapper.toRegisterResponse(userRepository.save(user));
    }

    public LoginResponse  login(LoginRequest request){
        return null;
    }
}
