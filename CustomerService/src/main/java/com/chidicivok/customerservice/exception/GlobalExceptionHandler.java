package com.chidicivok.customerservice.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // private helper method
    private ResponseEntity<ApiError> build(HttpStatus httpStatus, String message, HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(apiError);
    }

    @ExceptionHandler( {AuditLogServiceFailure.class, ExternalEmailApiException.class, InvalidArguementException.class, NotificationServiceFailure.class, UnExpectedFailureException.class})
    public ResponseEntity<ApiError> unknown(RuntimeException ex, HttpServletRequest request) {
        return build( HttpStatus.BAD_REQUEST , ex.getMessage(), request);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ApiError> duplicate(DuplicateException ex, HttpServletRequest request) {
        return build( HttpStatus.CONFLICT , ex.getMessage(), request);
    }

    @ExceptionHandler(NotPermittedException.class)
    public ResponseEntity<ApiError> unauthorized(NotPermittedException ex, HttpServletRequest request) {
        return build( HttpStatus.UNAUTHORIZED , ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> notFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return build( HttpStatus.NOT_FOUND , ex.getMessage(), request);
    }













}
