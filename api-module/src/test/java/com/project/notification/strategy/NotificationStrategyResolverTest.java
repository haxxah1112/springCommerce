package com.project.notification.strategy;

import com.project.UserFixture;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationStrategyResolverTest {
    @Mock
    private UserRepository userRepository;  // Mock UserRepository

    @InjectMocks
    private AllUsersStrategy allUsersStrategy;  // 의존성 주입된 AllUsersStrategy

    private NotificationStrategyResolver notificationStrategyResolver;

    @BeforeEach
    void setUp() {
        notificationStrategyResolver = new NotificationStrategyResolver(List.of(allUsersStrategy));
    }


    @Test
    void notificationStrategyResolverTest_Success() {
        //When
        NotificationStrategy strategy = notificationStrategyResolver.resolveStrategy("ALL");

        //Then
        assertTrue(strategy instanceof AllUsersStrategy);
        assertEquals("ALL", strategy.getKey());
    }

    @Test
    void notificationStrategyResolverTest_Fail() {
        //When
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                notificationStrategyResolver.resolveStrategy("INVALID_KEY"));

        //Then
        assertEquals("No strategy found for key: INVALID_KEY", exception.getMessage());
    }

    @Test
    void getTargetUsersTest() {
        //Given
        List<Users> mockUsers = List.of(UserFixture.createDefaultUser());
        when(userRepository.findAllByIsNotificationActive(true)).thenReturn(mockUsers);

        //When
        Flux<Users> targetUsers = allUsersStrategy.getTargetUsers();

        //Then
        assertEquals(mockUsers.size(), targetUsers.count().block());
    }
}