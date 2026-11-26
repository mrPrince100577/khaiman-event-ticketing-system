package com.stj.khaiman.user.dto.response;

import com.stj.khaiman.user.enums.UserStatus;

import java.util.List;

public record UserSummaryResponse(
    Long id,
    String email,
    String fullName,
    UserStatus userStatus,
    List<String> roles
) {
}
