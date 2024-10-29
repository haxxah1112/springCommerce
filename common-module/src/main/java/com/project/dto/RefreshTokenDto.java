package com.project.dto;

public record RefreshTokenDto(String token, Long refreshTokenExpiration) {}