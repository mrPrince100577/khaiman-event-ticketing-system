package com.stj.khaiman.user.controller;

import com.stj.khaiman.common.dto.response.ApiResponse;
import com.stj.khaiman.common.dto.response.OptionResponse;
import com.stj.khaiman.common.dto.response.PageResponse;
import com.stj.khaiman.user.dto.response.UserSummaryResponse;
import com.stj.khaiman.user.enums.UserStatus;
import com.stj.khaiman.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<ApiResponse<PageResponse<UserSummaryResponse>>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ){
        PageResponse<UserSummaryResponse> response = userService.getAllUsers(keyword, status, role, page, size, sortBy, direction);

        return ResponseEntity.ok(ApiResponse.success("Get user list success", response));
    }

    @GetMapping("/statuses")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<ApiResponse<List<OptionResponse>>> getAllUserStatuses() {
        return ResponseEntity.ok(ApiResponse.success("Get user statuses success", userService.getAllUserStatuses()));
    }
}
