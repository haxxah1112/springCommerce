package com.project.service;

import com.project.domain.Users.Users;
import com.project.dto.UserRegisterRequestDto;
import com.project.dto.UserRegisterResponseDto;

public interface UserService {

    UserRegisterResponseDto registerUser(UserRegisterRequestDto signUpRequest);
}
