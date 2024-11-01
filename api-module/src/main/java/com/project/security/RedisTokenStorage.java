package com.project.security;

import com.project.dto.RefreshTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
public class RedisTokenStorage implements TokenStorage {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveRefreshToken(JwtPayload jwtUserPayload, RefreshTokenDto refreshToken) {
        redisTemplate.opsForValue().set(
                jwtUserPayload.userId(),
                refreshToken.token(),
                refreshToken.refreshTokenExpiration(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get(userId);
    }

    @Override
    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(userId);
    }
}
