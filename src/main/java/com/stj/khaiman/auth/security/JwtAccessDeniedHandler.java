package com.stj.khaiman.auth.security;

import com.stj.khaiman.auth.exception.AuthenticationErrorCode;
import com.stj.khaiman.common.dto.response.ErrorResponse;
import com.stj.khaiman.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

//Capture 403 in filter layer: token hợp lệ nhưng user không đủ quyền
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.warn("Access denied: {}, path={}", accessDeniedException.getMessage(), request.getRequestURI());

        ErrorCode errorCode = AuthenticationErrorCode.ACCESS_DENIED;
        ErrorResponse body = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .timestamp(System.currentTimeMillis())
                .path(request.getRequestURI())
                .build();

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
