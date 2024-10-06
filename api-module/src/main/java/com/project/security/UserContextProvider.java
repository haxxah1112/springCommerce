package com.project.security;

import com.project.common.exception.NotFoundException;
import com.project.common.exception.error.CustomError;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class UserContextProvider {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Value("${service.jwt.header}")
    private String requestHeaderKey;

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

    public Users getCurrentUser() {
        Long userId = Long.valueOf(getCurrentUserId());
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));
    }
}
