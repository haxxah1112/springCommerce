package com.project.exception;


import com.project.exception.error.ErrorCode;

public class DuplicateException extends CustomException {
    public DuplicateException(String message) {
        super(message);
    }
    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
