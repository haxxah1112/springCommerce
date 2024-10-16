package com.project.security;

import com.project.domain.users.UserRole;
import com.project.exception.AuthenticationException;
import com.project.exception.error.AuthenticationError;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
public class TokenValidationAspect {
    private final SecretKey secretKey;

    @Value("${service.jwt.header}")
    private String requestHeaderKey;
    private final JwtProvider jwtProvider;

    public TokenValidationAspect(
            @Value("${service.jwt.secret-key}") String secretKey,
            JwtProvider jwtProvider
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtProvider = jwtProvider;
    }

    @Before("@annotation(validateToken)")
    public void validateJwtToken(JoinPoint joinPoint, ValidateToken validateToken) {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String token = request.getHeader(requestHeaderKey);
        token = token.replaceAll("Bearer ", "");

        JwtPayload jwtPayload = jwtProvider.verifyToken(token);

        UserRole userRole = jwtPayload.userRole();
        if (userRole != UserRole.ALL) {
            validatePermission(validateToken, userRole);
        }
    }

    private static void validatePermission(ValidateToken validateToken, UserRole userRole) {
        UserRole checkRole = validateToken.checkLevel();
        if (checkRole != userRole) {
            throw new AuthenticationException(AuthenticationError.NO_PERMISSION);
        }
    }

}
