package com.stj.khaiman.auth.dto.response;

import com.stj.khaiman.user.enums.UserStatus;

public record RegisterResponse(
        String username,
        String email,
        UserStatus status
) {
}
