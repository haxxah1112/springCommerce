package com.project.handler;

import com.project.common.dto.ApiResponse;
import com.project.exception.CustomException;
import com.project.exception.error.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse<?> response = ApiResponse.fail(ex.getMessage(), errorCode);
        HttpStatus status = errorCode.getStatus();
        return ResponseEntity.status(status).body(response);
    }
}
