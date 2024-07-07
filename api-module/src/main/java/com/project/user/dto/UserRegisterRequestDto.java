package com.project.user.dto;

import com.project.domain.Users.UserRole;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserRegisterRequestDto {
    @NotEmpty
    private String userName;

    @NotEmpty
    private String userEmail;

    @NotEmpty
    private String userPassword;

    private UserRole userRole = UserRole.BUYER;

}
