package com.project.security;

import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class UserContextProvider {
    private final JwtStorage jwtProvider;
    private final UserRepository userRepository;

    @Value("${service.jwt.header}")
    private String requestHeaderKey;

    public String getCurrentUserId() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        return request.getHeader("X-User-Id");
    }

    public Users getCurrentUser() {
        Long userId = Long.valueOf(getCurrentUserId());
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));
    }
}
