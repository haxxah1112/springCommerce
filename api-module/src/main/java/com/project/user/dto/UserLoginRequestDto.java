package com.project.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserLoginRequestDto {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
