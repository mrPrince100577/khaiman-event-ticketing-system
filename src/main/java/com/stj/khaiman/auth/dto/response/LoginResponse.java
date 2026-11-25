package com.stj.khaiman.auth.dto.response;

import com.stj.khaiman.user.dto.response.RoleResponse;
import lombok.Builder;

import java.util.Set;

@Builder
public record LoginResponse(
        String accessToken,
        String refreshToken,
        Set<RoleResponse> roles
) {
}
