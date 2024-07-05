package com.project.service;

import com.project.domain.Users.UserRole;
import com.project.domain.Users.Users;
import com.project.dto.UserRegisterRequestDto;
import com.project.dto.UserRegisterResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public Users convertRegisterToUserEntity(UserRegisterRequestDto registerRequestDto) {
        return Users.builder()
                .email(registerRequestDto.getUserEmail())
                .name(registerRequestDto.getUserName())
                .userRole(UserRole.BUYER)
                .build();
    }

    public UserRegisterResponseDto convertUserToRegisterResponse(Users user) {
        return new UserRegisterResponseDto(user.getId(), user.getName(), user.getEmail(), user.getUserRole());
    }
}
