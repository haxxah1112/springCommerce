package com.project.service;

import com.project.domain.Users.UserRole;
import com.project.domain.Users.Users;
import com.project.dto.UserRegisterRequestDto;
import com.project.dto.UserRegisterResponseDto;
import com.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Override
    public UserRegisterResponseDto registerUser(UserRegisterRequestDto signUpRequest) {
        Users user = Users.builder()
                .email(signUpRequest.getUserEmail())
                .name(signUpRequest.getUserName())
                .userRole(UserRole.BUYER)
                .build();
        return userConverter.convertUserToRegisterResponse(userRepository.save(user));
    }
}
