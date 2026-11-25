package com.stj.khaiman.auth.dto.request;

public record LoginRequest(
        String username,
        String password
) {
}
