package com.stj.khaiman.user.controller;

import com.stj.khaiman.common.dto.response.ApiResponse;
import com.stj.khaiman.common.dto.response.PageResponse;
import com.stj.khaiman.user.dto.response.UserSummaryResponse;
import com.stj.khaiman.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<ApiResponse<PageResponse<UserSummaryResponse>>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ){
        PageResponse<UserSummaryResponse> response = userService.getAllUsers(keyword, page, size, sortBy, direction);

        return ResponseEntity.ok(ApiResponse.success("Get user list success", response));
    }
}
