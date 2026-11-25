package com.stj.khaiman.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "Invalid token")
        String refreshToken
) {
}
