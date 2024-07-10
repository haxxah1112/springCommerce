package com.project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    @Value("${service.jwt.email-key}")
    private String emailKey;

    private final SecretKey secretKey;

    public JwtProvider(@Value("${service.jwt.secret-key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(JwtPayload jwtPayload) {

        return Jwts.builder()
                .claim("email", jwtPayload)
                .issuedAt(jwtPayload.issuedAt())
                .expiration(new Date(jwtPayload.issuedAt().getTime() + accessExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
