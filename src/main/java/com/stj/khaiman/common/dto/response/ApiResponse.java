package com.stj.khaiman.common.dto.response;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
        String status,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(String message, T data){
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .build();
    }

}
