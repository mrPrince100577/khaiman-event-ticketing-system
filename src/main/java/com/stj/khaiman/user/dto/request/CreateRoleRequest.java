package com.stj.khaiman.user.dto.request;

public record CreateRoleRequest(
        String roleName,
        String description,
        Boolean builtIn
) {
}
