package com.stj.khaiman.common.exception;

import org.springframework.http.HttpStatus;

public enum AppErrorCode implements ErrorCode{
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi hệ thống không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT(HttpStatus.BAD_REQUEST.value(), "Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "Chưa xác thực người dùng", HttpStatus.UNAUTHORIZED),
    USER_EXISTED(HttpStatus.BAD_REQUEST.value(), "Trùng tài khoản hoặc email, vui lòng nhập tafi khoản/email khác", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    AppErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
