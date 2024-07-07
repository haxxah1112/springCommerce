package com.project.user.service;

import com.project.user.dto.UserRegisterRequestDto;
import com.project.user.dto.UserRegisterResponseDto;

public interface UserService {

    UserRegisterResponseDto registerUser(UserRegisterRequestDto signUpRequest);
}
