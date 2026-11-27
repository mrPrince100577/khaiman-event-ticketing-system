package com.stj.khaiman.user.service;

import com.stj.khaiman.user.dto.response.RoleSummaryResponse;

import java.util.List;

public interface RoleService {
    List<RoleSummaryResponse> getAllRoles();
}
