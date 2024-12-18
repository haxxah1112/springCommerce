package com.project.notification.strategy;

import com.project.BrandFixture;
import com.project.OrderFixture;
import com.project.ProductFixture;
import com.project.UserFixture;
import com.project.common.message.DeliveryCompletionNotificationMessage;
import com.project.common.message.EventNotificationMessage;
import com.project.domain.brands.Brands;
import com.project.domain.order.OrderItems;
import com.project.domain.order.OrderStatus;
import com.project.domain.order.Orders;
import com.project.domain.order.repository.OrderItemRepository;
import com.project.domain.products.Categories;
import com.project.domain.products.Products;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.enums.NotificationTemplate;
import com.project.exception.NotFoundException;
import com.project.notification.sender.NotificationSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationStrategyResolverTest {
    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private NotificationSender notificationSender;

    private NotificationStrategyResolver notificationStrategyResolver;
    @Mock
    private NotificationTemplate notificationTemplate;

    @BeforeEach
    void setUp() {
        NotificationStrategy deliveryCompletionNotificationStrategy = new DeliveryCompletionNotificationStrategy(orderItemRepository, notificationSender);
        NotificationStrategy eventNotificationStrategy = new EventNotificationStrategy(notificationSender);

        notificationStrategyResolver = new NotificationStrategyResolver(
                List.of(deliveryCompletionNotificationStrategy, eventNotificationStrategy)
        );
    }

    @Test
    void notificationStrategyResolverTest_Success() {
        //When
        NotificationStrategy strategy = notificationStrategyResolver.resolveStrategy("DELIVERY_COMPLETION_TEMPLATE");

        //Then
        assertTrue(strategy instanceof DeliveryCompletionNotificationStrategy);
        assertEquals(NotificationTemplate.DELIVERY_COMPLETION_TEMPLATE, strategy.getTemplate());
    }

    @Test
    void notificationStrategyResolverTest_Fail() {
        // When
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                notificationStrategyResolver.resolveStrategy("INVALID_TEMPLATE"));

        // Then
        assertEquals("No strategy found for key: INVALID_TEMPLATE", exception.getMessage());
    }
}