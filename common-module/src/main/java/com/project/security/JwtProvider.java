package com.project.security;

import com.project.dto.RefreshTokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${service.jwt.refresh-expiration}")
    private Long refreshExpiration;

    private final SecretKey secretKey;

    public JwtProvider(@Value("${service.jwt.secret-key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(JwtPayload jwtPayload) {

        return Jwts.builder()
                .claim("userId", jwtPayload.userId())
                .issuedAt(jwtPayload.issuedAt())
                .expiration(new Date(jwtPayload.issuedAt().getTime() + accessExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public RefreshTokenDto generateRefreshToken(JwtPayload jwtPayload){
        String userId = jwtPayload.userId().toString();
        Claims claims = Jwts.claims().setSubject(userId).build();
        Date expireDate = new Date(jwtPayload.issuedAt().getTime() + refreshExpiration);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(jwtPayload.issuedAt())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return new RefreshTokenDto(refreshToken, refreshExpiration);
    }

    public boolean verifyToken(String jwtToken) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwtToken);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserIdByJwt(String jwtToken) {
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(jwtToken);

        return claimsJws.getBody().get("userId", String.class);
    }
}
