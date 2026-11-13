package com.stj.khaiman.common.dto.response;

import lombok.Builder;

@Builder
public record ErrorResponse(
        int code,
        String message,
        String error,
        long timestamp,
        String path
) {}
