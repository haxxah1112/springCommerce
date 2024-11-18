package com.project.user.exception;

import com.project.domain.users.UserRole;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.exception.AuthenticationException;
import com.project.exception.error.AuthenticationError;
import com.project.security.JwtProvider;
import com.project.security.RedisTokenStorage;
import com.project.user.dto.UserLoginRequestDto;
import com.project.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceImplExceptionTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtProvider jwtProvider;
    @Mock
    RedisTokenStorage redisTokenStorage;
    @InjectMocks
    UserServiceImpl userService;

    UserLoginRequestDto loginRequest;

    @BeforeEach
    public void setUp() {
        loginRequest = UserLoginRequestDto.builder()
                .email("test@naver.com")
                .password("password")
                .build();
    }

    @Test
    @DisplayName("로그인 시 사용자가 존재하지 않으면 예외가 발생한다")
    public void loginUser_UserNotFound() {
        //Given
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        //When
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            userService.loginUser(loginRequest);
        });

        //Then
        assertThat(exception.getMessage()).isEqualTo(AuthenticationError.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("로그인 시 비밀번호가 일치하지 않으면 예외가 발생한다")
    public void loginUser_InvalidPassword() {
        //Given
        Users user = Users.builder()
                .name("test")
                .email("test@naver.com")
                .password("password")
                .userRole(UserRole.BUYER)
                .build();

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        //When
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            userService.loginUser(loginRequest);
        });

        //Then
        assertThat(exception.getMessage()).isEqualTo(AuthenticationError.INVALID_PASSWORD.getMessage());
    }


    @Test
    @DisplayName("토큰 재발급 시 리프레시 토큰이 없으면 예외가 발생한다")
    void refreshToken_RefreshTokenNotFound() {
        //Given
        String refreshToken = "refreshToken";
        String userId = "user123";
        when(jwtProvider.extractToken(refreshToken)).thenReturn(refreshToken);
        when(jwtProvider.getUserIdByJwt(refreshToken)).thenReturn(userId);
        when(redisTokenStorage.getRefreshToken(userId)).thenReturn(null);

        //When
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            userService.refreshToken(refreshToken);
        });

        //Then
        assertEquals(AuthenticationError.REFRESH_TOKEN_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("리프레시 토큰이 유효하지 않으면 예외가 발생한다")
    void refreshToken_InvalidRefreshToken() {
        //Given
        String refreshToken = "refreshToken";
        String userId = "user123";
        String storedRefreshToken = "storedRefreshToken";

        when(jwtProvider.extractToken(refreshToken)).thenReturn(refreshToken);
        when(jwtProvider.getUserIdByJwt(refreshToken)).thenReturn(userId);
        when(redisTokenStorage.getRefreshToken(userId)).thenReturn(storedRefreshToken);
        when(jwtProvider.verifyToken(storedRefreshToken)).thenReturn(false);

        //When
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            userService.refreshToken(refreshToken);
        });

        //Then
        assertEquals(AuthenticationError.INVALID_REFRESH_TOKEN, exception.getErrorCode());
    }
}
