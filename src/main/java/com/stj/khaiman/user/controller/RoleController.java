package com.stj.khaiman.user.controller;

import com.stj.khaiman.common.dto.response.ApiResponse;
import com.stj.khaiman.user.dto.response.RoleSummaryResponse;
import com.stj.khaiman.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<ApiResponse<List<RoleSummaryResponse>>> getAllRoles(){
        List<RoleSummaryResponse> response = roleService.getAllRoles();
        return ResponseEntity.ok(ApiResponse.success("Get all roles success", response));
    }
}
