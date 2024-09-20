package com.project.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserContextProvider {
    private final JwtProvider jwtProvider;

    @Value("${service.jwt.header}")
    private String requestHeaderKey;

    public UserContextProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public String getCurrentUserId() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String token = request.getHeader(requestHeaderKey);
        token = token.replaceAll("Bearer ", "");

        JwtPayload jwtPayload = jwtProvider.verifyToken(token);

        Long userId = jwtPayload.userId();
        return userId.toString();
    }
}
