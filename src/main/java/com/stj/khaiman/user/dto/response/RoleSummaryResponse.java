package com.stj.khaiman.user.dto.response;

import lombok.Builder;

@Builder
public record RoleSummaryResponse(
        String roleName,
        String description,
        Boolean builtIn
) {
}
