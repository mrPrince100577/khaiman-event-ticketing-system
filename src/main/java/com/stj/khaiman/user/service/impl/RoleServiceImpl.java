package com.stj.khaiman.user.service.impl;

import com.stj.khaiman.user.dto.response.RoleSummaryResponse;
import com.stj.khaiman.user.mapper.RoleMapper;
import com.stj.khaiman.user.repository.RoleRepository;
import com.stj.khaiman.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleSummaryResponse> getAllRoles() {

        return roleRepository.findAll().stream()
                .map(roleMapper::toRoleSummaryResponse)
                .toList();
    }
}
