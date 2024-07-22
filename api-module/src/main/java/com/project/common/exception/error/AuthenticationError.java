package com.project.common.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthenticationError implements ErrorCode{
    EMAIL_ALREADY_EXIST("SIGNUP_001", "email is already exist"),
    USER_NOT_FOUND("LOGIN_001", "request email is not found"),
    INVALID_PASSWORD("LOGIN_002", "password is not valid with email"),
    NO_PERMISSION("AUTH_001", "no permission"),
    INVALID_TOKEN("AUTH_002", "invalid token");

    private String code;
    private String message;
}
