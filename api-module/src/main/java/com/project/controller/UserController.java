package com.project.controller;

import com.project.domain.Users.Users;
import com.project.dto.UserRegisterRequestDto;
import com.project.dto.UserRegisterResponseDto;
import com.project.service.UserService;
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
    public ResponseEntity<UserRegisterResponseDto> signUpUser(@Valid @RequestBody UserRegisterRequestDto signUpRequest) {
        UserRegisterResponseDto signUpResponse = userService.registerUser(signUpRequest);
        return ResponseEntity.ok(signUpResponse);
    }
}
