package com.project.user.service;

import com.project.common.ApiResponse;
import com.project.domain.users.Users;
import com.project.security.JwtPayload;
import com.project.security.JwtProvider;
import com.project.user.dto.UserLoginRequestDto;
import com.project.user.dto.UserLoginResponseDto;
import com.project.user.dto.UserRegisterRequestDto;
import com.project.user.dto.UserRegisterResponseDto;
import com.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public ApiResponse<UserRegisterResponseDto> registerUser(UserRegisterRequestDto registerRequest) {
        validateEmailNotDuplicate(registerRequest.getUserEmail());
        Users user = userConverter.convertRegisterToUserEntity(registerRequest);
        return ApiResponse.success(userConverter.convertUserToRegisterResponse(userRepository.save(user)));
    }

    private void validateEmailNotDuplicate(String email) {
        boolean isPresent = userRepository.findByEmail(email).isPresent();
        if (isPresent) {
            log.info("{} is already exist", email);
            throw new RuntimeException("email is already exist");
        }
    }

    @Override
    public ApiResponse<UserLoginResponseDto> loginUser(UserLoginRequestDto loginRequest) {
        Users findUser = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new RuntimeException("request email is not found")
        );

        Boolean isMatched = passwordEncoder.matches(loginRequest.getPassword(), findUser.getPassword());
        if (!isMatched) {
            throw new RuntimeException("password is not valid with email");
        }

        JwtPayload jwtPayload = new JwtPayload(loginRequest.getEmail(), new Date());
        String token = jwtProvider.generateToken(jwtPayload);

        return ApiResponse.success(new UserLoginResponseDto(findUser.getName(), findUser.getEmail(), token));
    }
}
