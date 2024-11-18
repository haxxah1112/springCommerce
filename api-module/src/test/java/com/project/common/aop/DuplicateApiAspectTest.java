package com.project.common.aop;

import com.project.security.UserContextProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DuplicateApiAspectTest {
    @Mock
    private RedissonClient redissonClient;

    @Mock
    private UserContextProvider userContextProvider;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @Mock
    private RLock lock;

    private DuplicateApiAspect duplicateApiAspect;

    @BeforeEach
    public void setup() {
        duplicateApiAspect = new DuplicateApiAspect(redissonClient, userContextProvider);
    }

    @Test
    public void manageConcurrency_SuccessfulLock() throws Throwable {
        //Given
        when(userContextProvider.getCurrentUserId()).thenReturn("user123");
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getName()).thenReturn("testMethod");
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(lock.tryLock(5, 30, TimeUnit.SECONDS)).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);
        Object expectedResult = new Object();
        when(joinPoint.proceed()).thenReturn(expectedResult);

        //When
        Object result = duplicateApiAspect.manageConcurrency(joinPoint, null);

        //Then
        assertEquals(expectedResult, result);
        verify(lock).unlock();
    }

    @Test
    public void manageConcurrency_FailedLock() throws Throwable {
        //Given
        when(userContextProvider.getCurrentUserId()).thenReturn("user123");
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getName()).thenReturn("testMethod");
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(lock.tryLock(5, 30, TimeUnit.SECONDS)).thenReturn(false);

        //When
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            duplicateApiAspect.manageConcurrency(joinPoint, null);
        });

        //Then
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, exception.getStatusCode());
        verify(joinPoint, never()).proceed();
        verify(lock, never()).unlock();
    }
}