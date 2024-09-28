package com.project.common.exception;

import com.project.common.exception.error.ErrorCode;

public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public CustomException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }
    public CustomException(String message) {
        super(message);
        this.errorCode = null;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

}
