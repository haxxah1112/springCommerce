package com.project.common.exception;


import com.project.common.exception.error.ErrorCode;

public class DuplicateException extends CustomException {
    public DuplicateException(String message) {
        super(message);
    }
    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
