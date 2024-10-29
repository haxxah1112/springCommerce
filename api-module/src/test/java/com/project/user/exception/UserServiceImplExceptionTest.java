package com.project.user.exception;

import com.project.domain.users.UserRole;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.exception.AuthenticationException;
import com.project.exception.error.AuthenticationError;
import com.project.user.dto.UserLoginRequestDto;
import com.project.user.service.UserServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceImplExceptionTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
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
}
