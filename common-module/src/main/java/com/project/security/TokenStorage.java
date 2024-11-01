package com.project.security;

import com.project.dto.RefreshTokenDto;

public interface TokenStorage {
    void saveRefreshToken(JwtPayload jwtUserPayload, RefreshTokenDto refreshToken);
    String getRefreshToken(String userId);
    void deleteRefreshToken(String userId);
}
