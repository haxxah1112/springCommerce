package com.project.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    enum ResponseStatus {
        SUCCESS, FAIL
    }
    private ResponseStatus status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResponseStatus.SUCCESS, "success", data);
    }

    public static ApiResponse<?> fail(String message) {
        return new ApiResponse<>(ResponseStatus.FAIL, message, null);
    }
}
