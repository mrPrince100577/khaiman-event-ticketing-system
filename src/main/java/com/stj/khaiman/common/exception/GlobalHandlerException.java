package com.stj.khaiman.common.exception;

import com.stj.khaiman.auth.exception.AuthenticationErrorCode;
import com.stj.khaiman.common.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {

    //Handle app's business cases
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex, WebRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("AppException: code={}, path={}", errorCode.getCode(), path(request));
        return build(errorCode, errorCode.getMessage(), request);
    }

    //handle annotation @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation failed: {}, path={}", message, path(request));
        return build(AppErrorCode.INVALID_INPUT, message, request);
    }

    //handle wrong json format & input wrong enum value(use when using postman, curl,...)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        String message = AppErrorCode.INVALID_INPUT_FORMAT.getMessage();
        if (ex.getCause() instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            message = enumMessage(fieldPath(ife), ife.getValue(), ife.getTargetType());
        }
        log.warn("Unreadable request body: {}, path={}", ex.getMessage(), path(request));
        return build(AppErrorCode.INVALID_INPUT_FORMAT, message, request);
    }

    //handle wrong param type in url
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        Class<?> type = ex.getRequiredType();
        String message = (type != null && type.isEnum())
                ? enumMessage(ex.getName(), ex.getValue(), type)
                : "The value '%s' is invalid for parameter '%s'".formatted(ex.getValue(), ex.getName());
        return build(AppErrorCode.INVALID_INPUT, message, request);
    }

    //handle missing @RequestParam
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex, WebRequest request) {
        return build(AppErrorCode.INVALID_INPUT, "Missing parameter '%s'".formatted(ex.getParameterName()), request);
    }

    //Url not found
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoResourceFoundException ex, WebRequest request) {
        return build(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "Resource not found", request);
    }

    //wrong http method
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method %s is not supported".formatted(ex.getMethod()), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception ex, WebRequest request) {
        log.error("Unhandled exception at path={}", path(request), ex);   // ex ở cuối nên in cả stack trace
        return build(AppErrorCode.INTERNAL_SERVER_ERROR, AppErrorCode.INTERNAL_SERVER_ERROR.getMessage(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        log.warn("Access denied: path={}", path(request));
        return build(AuthenticationErrorCode.ACCESS_DENIED, AuthenticationErrorCode.ACCESS_DENIED.getMessage(), request);
    }

    // Hàm helper
    private ResponseEntity<ErrorResponse> build(ErrorCode errorCode, String message, WebRequest request) {
        return build(errorCode.getHttpStatus(), errorCode.getCode(), message, request);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, int code, String message, WebRequest request) {
        ErrorResponse body = ErrorResponse.builder()
                .code(code)
                .message(message)
                .error(status.getReasonPhrase())
                .timestamp(System.currentTimeMillis())
                .path(path(request))
                .build();
        return ResponseEntity.status(status).body(body);
    }

    private String path(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

    private String fieldPath(InvalidFormatException ife) {
        return ife.getPath().stream()
                .map(JacksonException.Reference::getPropertyName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("."));
    }

    private String enumMessage(String field, Object value, Class<?> enumType) {
        String allowed = Arrays.stream(enumType.getEnumConstants())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        return "The value '%s' is invalid for the '%s' field. Valid values: %s".formatted(value, field, allowed);
    }
}
