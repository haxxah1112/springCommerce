package com.project.user.controller;

import com.project.common.ApiResponse;
import com.project.user.dto.UserLoginRequestDto;
import com.project.user.dto.UserRegisterRequestDto;
import com.project.user.service.UserService;
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
    public ResponseEntity<ApiResponse> signUpUser(@Valid @RequestBody UserRegisterRequestDto registerRequest) {
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@Valid @RequestBody UserLoginRequestDto loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }
}
