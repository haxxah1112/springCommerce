package com.project.security;

import com.project.domain.users.UserRole;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.exception.AuthenticationException;
import com.project.exception.NotFoundException;
import com.project.exception.error.AuthenticationError;
import com.project.exception.error.CustomError;
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
public class RoleValidationAspect {
    private final SecretKey secretKey;

    @Value("${service.jwt.header}")
    private String requestHeaderKey;
    private final UserRepository userRepository;

    public RoleValidationAspect(
            @Value("${service.jwt.secret-key}") String secretKey,
            UserRepository userRepository
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.userRepository = userRepository;
    }

    @Before("@annotation(validateToken)")
    public void validateJwtToken(JoinPoint joinPoint, CheckRole validateToken) {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String userId = request.getHeader("X-User-Id");

        Users user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));

        UserRole userRole = user.getUserRole();
        if (userRole != UserRole.ALL) {
            validatePermission(validateToken, userRole);
        }
    }

    private static void validatePermission(CheckRole validateToken, UserRole userRole) {
        UserRole checkRole = validateToken.accessLevel();
        if (checkRole != userRole) {
            throw new AuthenticationException(AuthenticationError.NO_PERMISSION);
        }
    }

}
