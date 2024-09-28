package com.project.common.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomError implements ErrorCode {
    DUPLICATE("This data already exists.", HttpStatus.CONFLICT),
    NOT_FOUND("No matching data found for the provided ID.", HttpStatus.NOT_FOUND);

    private String message;
    private HttpStatus status;

    @Override
    public HttpStatus getStatus() {
        return this.getStatus();
    }

    @Override
    public String getName() {
        return this.name();
    }
}
