package com.stj.khaiman.auth.exception;

import com.stj.khaiman.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthenticationErrorCode implements ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", HttpStatus.UNAUTHORIZED),
    USER_EXISTED(HttpStatus.BAD_REQUEST.value(), "Username/Email existed, please change it", HttpStatus.BAD_REQUEST),
    ACCOUNT_BANNED(HttpStatus.FORBIDDEN.value(),"Your account is banned by the system, please go to recover account to access this account" , HttpStatus.FORBIDDEN),
    ACCOUNT_INACTIVE(HttpStatus.FORBIDDEN.value(), "Your account isn't activate yet" , HttpStatus.FORBIDDEN),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "Your session is no longer valid! Please login again!", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "You don't have permission to access this resource", HttpStatus.FORBIDDEN);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    AuthenticationErrorCode(int code, String message, HttpStatus httpStatus) {
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
