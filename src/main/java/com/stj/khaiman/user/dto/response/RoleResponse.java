package com.stj.khaiman.user.dto.response;

import java.util.Set;

public record RoleResponse(
        String roleName,
        String description,
        Boolean builtIn,
        Set<PermissionResponse> permissions
) {
}
