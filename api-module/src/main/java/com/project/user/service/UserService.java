package com.project.user.service;

import com.project.common.dto.ApiResponse;
import com.project.user.dto.UserLoginRequestDto;
import com.project.user.dto.UserLoginResponseDto;
import com.project.user.dto.UserRegisterRequestDto;
import com.project.user.dto.UserRegisterResponseDto;

public interface UserService {

    ApiResponse<UserRegisterResponseDto> registerUser(UserRegisterRequestDto signUpRequest);

    ApiResponse<UserLoginResponseDto> loginUser(UserLoginRequestDto loginRequest);
}
