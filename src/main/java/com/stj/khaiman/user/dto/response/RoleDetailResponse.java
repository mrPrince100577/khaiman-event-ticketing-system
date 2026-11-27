package com.stj.khaiman.user.dto.response;

import java.util.Set;

public record RoleDetailResponse(
        String roleName,
        String description,
        Boolean builtIn,
        Set<PermissionResponse> permissions
) {
}
