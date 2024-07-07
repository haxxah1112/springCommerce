package com.project.user.controller;

import com.project.user.dto.UserLoginRequestDto;
import com.project.user.dto.UserRegisterRequestDto;
import com.project.user.dto.UserRegisterResponseDto;
import com.project.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> signUpUser(@Valid @RequestBody UserRegisterRequestDto registerRequest) {
        UserRegisterResponseDto registerResponse = userService.registerUser(registerRequest);
        return ResponseEntity.ok(registerResponse);
    }
}
