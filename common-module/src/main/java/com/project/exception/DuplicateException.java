package com.project.exception;


import com.project.exception.error.ErrorCode;

public class DuplicateException extends CustomException {
    public DuplicateException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
