package com.project.user.dto;

import com.project.domain.users.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegisterResponseDto {
    private Long id;
    private String name;
    private String email;
    private UserRole userRole;
}
