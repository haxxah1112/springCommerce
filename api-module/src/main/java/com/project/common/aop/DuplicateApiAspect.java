package com.project.common.aop;

import com.project.common.annotation.PreventDuplicate;
import com.project.security.JwtPayload;
import com.project.security.JwtProvider;
import com.project.security.UserContextProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class DuplicateApiAspect {
    private final RedissonClient redissonClient;

    @Value("${service.jwt.header}")
    private String requestHeaderKey;

    private final JwtProvider jwtProvider;

    @Around("@annotation(preventDuplicate)")
    public Object manageConcurrency(ProceedingJoinPoint joinPoint, PreventDuplicate preventDuplicate) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String userId = getCurrentUserId();
        String lockKey = "lock:" + userId + ":" + methodSignature.getName();

        RLock lock = redissonClient.getLock(lockKey);

        boolean locked = lock.tryLock(5, 30, TimeUnit.SECONDS);

        if (locked) {
            try {
                return joinPoint.proceed();
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        } else {
            throw new RuntimeException("Duplicate request");
        }
    }

    private String getCurrentUserId() {
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
