package com.stj.khaiman.user.service;

import com.stj.khaiman.common.dto.response.OptionResponse;
import com.stj.khaiman.common.dto.response.PageResponse;
import com.stj.khaiman.user.dto.response.UserSummaryResponse;
import com.stj.khaiman.user.enums.UserStatus;

import java.util.List;

public interface UserService {

     PageResponse<UserSummaryResponse> getAllUsers(
            String keyword,
            UserStatus userStatus,
            String role,
            int page,
            int size,
            String sortBy,
            String direction
    );

     List<OptionResponse> getAllUserStatuses();
}

