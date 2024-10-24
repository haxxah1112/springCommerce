package com.project.notification.service;

import com.project.UserFixture;
import com.project.domain.users.UserRole;
import com.project.domain.users.Users;
import com.project.handler.NotificationHandler;
import com.project.notification.dto.NotificationSendRequestDto;
import com.project.notification.strategy.NotificationStrategy;
import com.project.notification.strategy.NotificationStrategyResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationStrategyResolver notificationStrategyResolver;

    @Mock
    private NotificationHandler notificationHandler;

    @Mock
    private NotificationStrategy notificationStrategy;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void NotificationServiceTest() {
        //Given
        Users user1 = UserFixture.createUser("test1", "test1@test.com", UserRole.BUYER);
        Users user2 = UserFixture.createUser("test2", "test2@test.com", UserRole.BUYER);
        Users user3 = UserFixture.createUser("test3", "test3@test.com", UserRole.BUYER);

        NotificationSendRequestDto request = new NotificationSendRequestDto("Test", "ALL");
        NotificationStrategy strategy = mock(NotificationStrategy.class);
        List<Users> usersList = List.of(user1, user2, user3);

        ConcurrentHashMap<String, String> executionThreads = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(usersList.size());

        when(notificationStrategyResolver.resolveStrategy("ALL")).thenReturn(notificationStrategy);
        when(notificationStrategy.getTargetUsers())
                .thenReturn(Flux.fromIterable(usersList));

        when(notificationHandler.sendNotificationToUser(any(), any()))
                .thenAnswer(invocation -> {
                    Users user = invocation.getArgument(0);
                    String threadName = Thread.currentThread().getName();
                    executionThreads.put(user.getName(), threadName);
                    latch.countDown();
                    return Mono.empty();
                });

        //When
        notificationService.sendNotification(request);

        //Then
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            latch.await();

            assertEquals(usersList.size(), executionThreads.size());

            assertTrue(executionThreads.values()
                    .stream()
                    .allMatch(threadName -> threadName.contains("boundedElastic")));

            verify(notificationHandler, times(usersList.size()))
                    .sendNotificationToUser(any(), any());
        });
    }
}