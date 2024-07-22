package com.project.security;

import com.project.common.exception.AuthenticationException;
import com.project.common.exception.error.AuthenticationError;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.project.domain.users.UserRole.findUserRole;

@Component
public class JwtProvider {

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${service.jwt.email-key}")
    private String emailKey;

    @Value("${service.jwt.refresh-expiration}")
    private Long refreshExpiration;

    private final SecretKey secretKey;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtProvider(@Value("${service.jwt.secret-key}") String secretKey, RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(JwtPayload jwtPayload) {

        return Jwts.builder()
                .claim("email", jwtPayload.email())
                .claim("userRole", jwtPayload.userRole())
                .issuedAt(jwtPayload.issuedAt())
                .expiration(new Date(jwtPayload.issuedAt().getTime() + accessExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(JwtPayload jwtPayload){
        Claims claims = Jwts.claims().setSubject(jwtPayload.email()).build();
        Date expireDate = new Date(jwtPayload.issuedAt().getTime() + refreshExpiration);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(jwtPayload.issuedAt())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        redisTemplate.opsForValue().set(
                jwtPayload.email(),
                refreshToken,
                refreshExpiration,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    public JwtPayload verifyToken(String jwtToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(jwtToken);

            return new JwtPayload(claimsJws.getPayload().get(emailKey, String.class),
                    claimsJws.getPayload().getIssuedAt(),
                    findUserRole(claimsJws.getPayload().get("userRole", String.class)));
        } catch (JwtException e) {
            throw new AuthenticationException(AuthenticationError.INVALID_TOKEN, e);
        }
    }
}
