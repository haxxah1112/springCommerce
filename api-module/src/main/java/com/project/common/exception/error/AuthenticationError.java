package com.project.common.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthenticationError implements ErrorCode {
    EMAIL_ALREADY_EXIST("SIGNUP_001", "Email is already exist", HttpStatus.CONFLICT),
    USER_NOT_FOUND("LOGIN_001", "Request email is not found", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("LOGIN_002", "Password is not valid with email", HttpStatus.UNAUTHORIZED),
    NO_PERMISSION("AUTH_001", "No permission", HttpStatus.FORBIDDEN),
    INVALID_TOKEN("AUTH_002", "Invalid token", HttpStatus.UNAUTHORIZED);

    private String code;
    private String message;
    private HttpStatus status;

    @Override
    public HttpStatus getStatus() {
        return this.status;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
