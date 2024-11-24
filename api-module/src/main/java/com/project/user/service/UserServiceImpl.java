package com.project.user.service;

import com.project.common.dto.ApiResponse;
import com.project.domain.users.Users;
import com.project.dto.RefreshTokenDto;
import com.project.exception.AuthenticationException;
import com.project.exception.error.AuthenticationError;
import com.project.security.RedisTokenStorage;
import com.project.security.JwtPayload;
import com.project.security.JwtProvider;
import com.project.user.dto.UserLoginRequestDto;
import com.project.user.dto.UserLoginResponseDto;
import com.project.user.dto.UserRegisterRequestDto;
import com.project.user.dto.UserRegisterResponseDto;
import com.project.domain.users.repository.UserRepository;
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
    private final RedisTokenStorage redisTokenStorage;
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
            throw new AuthenticationException(AuthenticationError.EMAIL_ALREADY_EXIST);
        }
    }

    @Override
    public ApiResponse<UserLoginResponseDto> loginUser(UserLoginRequestDto loginRequest) {
        Users findUser = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new AuthenticationException(AuthenticationError.USER_NOT_FOUND)
        );

        Boolean isMatched = passwordEncoder.matches(loginRequest.getPassword(), findUser.getPassword());
        if (!isMatched) {
            throw new AuthenticationException(AuthenticationError.INVALID_PASSWORD);
        }

        JwtPayload jwtPayload = new JwtPayload(String.valueOf(findUser.getId()), new Date());
        String accessToken = jwtProvider.generateAccessToken(jwtPayload);

        RefreshTokenDto refreshToken = jwtProvider.generateRefreshToken(jwtPayload);
        redisTokenStorage.saveRefreshToken(jwtPayload, refreshToken);


        return ApiResponse.success(new UserLoginResponseDto(findUser.getName(), findUser.getEmail(), accessToken, refreshToken.token()));
    }

    @Override
    public ApiResponse<String> refreshToken(String refreshToken) {
        String token = jwtProvider.extractToken(refreshToken);

        String userId = jwtProvider.getUserIdByJwt(token);
        String storedRefreshToken = redisTokenStorage.getRefreshToken(userId);

        if (storedRefreshToken == null) {
            throw new AuthenticationException(AuthenticationError.REFRESH_TOKEN_NOT_FOUND);
        }

        if (!jwtProvider.verifyToken(storedRefreshToken)) {
            throw new AuthenticationException(AuthenticationError.INVALID_REFRESH_TOKEN);
        }

        JwtPayload jwtPayload = new JwtPayload(userId, new Date());
        String newAccessToken = jwtProvider.generateAccessToken(jwtPayload);

        return ApiResponse.success(newAccessToken);
    }
}
