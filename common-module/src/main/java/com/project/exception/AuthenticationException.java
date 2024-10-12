package com.project.exception;

import com.project.exception.error.ErrorCode;

public class AuthenticationException extends CustomException {
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
    public AuthenticationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
    }
}
