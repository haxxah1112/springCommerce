package com.project.user.service;

import com.project.common.dto.ApiResponse;
import com.project.domain.users.UserRole;
import com.project.domain.users.Users;
import com.project.dto.RefreshTokenDto;
import com.project.security.JwtProvider;
import com.project.security.JwtPayload;
import com.project.security.RedisTokenStorage;
import com.project.user.dto.UserLoginRequestDto;
import com.project.user.dto.UserLoginResponseDto;
import com.project.user.dto.UserRegisterRequestDto;
import com.project.domain.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserConverter userConverter;
    @Mock
    JwtProvider jwtProvider;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RedisTokenStorage redisTokenStorage;
    @InjectMocks
    UserServiceImpl userService;

    UserRegisterRequestDto request;
    UserLoginRequestDto loginRequest;
    @BeforeEach
    public void setUp() {
         request = UserRegisterRequestDto.builder()
                .userName("test")
                .userEmail("test@naver.com")
                .userPassword("password")
                .userRole(UserRole.BUYER)
                .build();

        loginRequest = UserLoginRequestDto.builder()
                .email("test@naver.com")
                .password("password")
                .build();
    }

    @Test
    public void registerUserTest() {
        //Given
        Users user = Users.builder()
                .name(request.getUserName())
                .email(request.getUserEmail())
                .password("password")
                .userRole(UserRole.BUYER)
                .build();

        //When
        when(userConverter.convertRegisterToUserEntity(request)).thenReturn(user);
        when(userRepository.save(any(Users.class))).thenReturn(user);
        when(userRepository.findByEmail(request.getUserEmail())).thenReturn(Optional.empty())
                .thenReturn(Optional.of(user));

        userService.registerUser(request);

        //Then
        Users savedUser = userRepository.findByEmail(request.getUserEmail()).get();

        assertThat(savedUser.getName()).isEqualTo(request.getUserName());
        assertThat(savedUser.getEmail()).isEqualTo(request.getUserEmail());
        assertThat(savedUser.getUserRole()).isEqualTo(request.getUserRole());
    }

    @Test
    public void loginUserTest() {
        //Given
        Users user = Users.builder()
                .name("test")
                .email("test@naver.com")
                .password("password")
                .userRole(UserRole.BUYER)
                .build();

        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(refreshToken, 1000L);

        //When
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtProvider.generateAccessToken(any(JwtPayload.class))).thenReturn(accessToken);
        when(jwtProvider.generateRefreshToken(any(JwtPayload.class))).thenReturn(refreshTokenDto);

        ApiResponse<UserLoginResponseDto> response = userService.loginUser(loginRequest);

        //Then
        assertThat(response.getData().getAccessToken()).isEqualTo(accessToken);
        assertThat(response.getData().getUserEmail()).isEqualTo(user.getEmail());
        assertThat(response.getData().getUserName()).isEqualTo(user.getName());
    }

    @Test
    public void refreshTokenTest() {
        String refreshToken = "refreshToken";
        String userId = "1";
        String storedRefreshToken = "storedRefreshToken";
        String newAccessToken = "newAccessToken";

        when(jwtProvider.extractToken(refreshToken)).thenReturn(refreshToken);
        when(jwtProvider.getUserIdByJwt(refreshToken)).thenReturn(userId);
        when(redisTokenStorage.getRefreshToken(userId)).thenReturn(storedRefreshToken);
        when(jwtProvider.verifyToken(storedRefreshToken)).thenReturn(true);
        when(jwtProvider.generateAccessToken(any(JwtPayload.class))).thenReturn(newAccessToken);


        ApiResponse<String> response = userService.refreshToken(refreshToken);

        assertEquals("newAccessToken", response.getData());
        verify(redisTokenStorage).getRefreshToken(userId);
    }
}