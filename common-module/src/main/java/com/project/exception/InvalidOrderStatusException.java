package com.project.exception;

import com.project.exception.error.ErrorCode;

public class InvalidOrderStatusException extends CustomException {
    public InvalidOrderStatusException(ErrorCode errorCode) {
        super(errorCode);
    }
}
