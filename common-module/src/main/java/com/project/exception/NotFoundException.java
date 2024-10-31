package com.project.exception;

import com.project.exception.error.ErrorCode;
import org.springframework.data.crossstore.ChangeSetPersister;

public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
