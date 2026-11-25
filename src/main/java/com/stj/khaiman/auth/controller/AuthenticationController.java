package com.stj.khaiman.auth.controller;

import com.stj.khaiman.auth.dto.request.LoginRequest;
import com.stj.khaiman.auth.dto.request.RefreshTokenRequest;
import com.stj.khaiman.auth.dto.request.RegisterRequest;
import com.stj.khaiman.auth.dto.response.LoginResponse;
import com.stj.khaiman.auth.dto.response.RegisterResponse;
import com.stj.khaiman.auth.service.AuthenticationService;
import com.stj.khaiman.common.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request){
        RegisterResponse response = authenticationService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Register success", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request){
        LoginResponse response = authenticationService.login(request);
        return  ResponseEntity.ok(ApiResponse.success("Login success", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(ApiResponse.success(authenticationService.logout(jwt), null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request){
        LoginResponse response = authenticationService.refreshToken(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.success("Refresh Token success!", response));
    }
}
