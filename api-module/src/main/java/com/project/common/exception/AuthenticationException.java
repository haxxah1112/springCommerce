package com.project.common.exception;

import com.project.common.exception.error.ErrorCode;

public class AuthenticationException extends CustomException {
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}