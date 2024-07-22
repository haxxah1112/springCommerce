package com.project.common.exception;

import com.project.common.exception.error.ErrorCode;

public class CustomException extends RuntimeException {
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
    public CustomException(String message) {
        super(message);
    }
    public CustomException() {}
}
