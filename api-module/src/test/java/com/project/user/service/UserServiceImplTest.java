package com.project.user.service;

import com.project.domain.users.UserRole;
import com.project.domain.users.Users;
import com.project.user.dto.UserRegisterRequestDto;
import com.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserConverter userConverter;
    @InjectMocks
    UserServiceImpl userService;

    UserRegisterRequestDto request;
    @BeforeEach
    public void setUp() {
         request = UserRegisterRequestDto.builder()
                .userName("test")
                .userEmail("test@naver.com")
                .userPassword("password")
                .userRole(UserRole.BUYER)
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

}