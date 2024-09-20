package com.project.common.aop;

import com.project.common.annotation.PreventDuplicate;
import com.project.security.UserContextProvider;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class DuplicateApiAspect {
    private final RedissonClient redissonClient;

    private final UserContextProvider userContextProvider;

    @Around("@annotation(preventDuplicate)")
    public Object manageConcurrency(ProceedingJoinPoint joinPoint, PreventDuplicate preventDuplicate) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String userId = userContextProvider.getCurrentUserId();
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
}
