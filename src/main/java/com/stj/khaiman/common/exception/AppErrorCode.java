package com.stj.khaiman.common.exception;

import org.springframework.http.HttpStatus;

public enum AppErrorCode implements ErrorCode{
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unknown Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT(HttpStatus.BAD_REQUEST.value(), "Invalid input request", HttpStatus.BAD_REQUEST),
    INVALID_INPUT_FORMAT(HttpStatus.BAD_REQUEST.value(), "Invalid input format request", HttpStatus.BAD_REQUEST),
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
