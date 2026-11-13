package com.stj.khaiman.auth.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String role
) {
}
