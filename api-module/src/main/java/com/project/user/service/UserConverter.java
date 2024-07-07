package com.project.user.service;

import com.project.domain.Users.UserRole;
import com.project.domain.Users.Users;
import com.project.user.dto.UserRegisterRequestDto;
import com.project.user.dto.UserRegisterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final PasswordEncoder passwordEncoder;

    public Users convertRegisterToUserEntity(UserRegisterRequestDto registerRequestDto) {
        return Users.builder()
                .email(registerRequestDto.getUserEmail())
                .name(registerRequestDto.getUserName())
                .password(passwordEncoder.encode(registerRequestDto.getUserPassword()))
                .userRole(UserRole.BUYER)
                .build();
    }

    public UserRegisterResponseDto convertUserToRegisterResponse(Users user) {
        return new UserRegisterResponseDto(user.getId(), user.getName(), user.getEmail(), user.getUserRole());
    }
}
