package com.project.user.service;

import com.project.domain.Users.Users;
import com.project.user.dto.UserRegisterRequestDto;
import com.project.user.dto.UserRegisterResponseDto;
import com.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Override
    public UserRegisterResponseDto registerUser(UserRegisterRequestDto registerRequest) {
        validateEmailNotDuplicate(registerRequest.getUserEmail());
        Users user = userConverter.convertRegisterToUserEntity(registerRequest);
        return userConverter.convertUserToRegisterResponse(userRepository.save(user));
    }

    private void validateEmailNotDuplicate(String email) {
        boolean isPresent = userRepository.findByEmail(email).isPresent();
        if (isPresent) {
            log.info("{} is already exist", email);
            throw new RuntimeException("email is already exist");
        }
    }
}
