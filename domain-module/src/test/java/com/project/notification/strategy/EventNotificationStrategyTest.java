package com.project.notification.strategy;

import com.project.common.message.EventNotificationMessage;
import com.project.domain.order.repository.OrderItemRepository;
import com.project.notification.sender.NotificationSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventNotificationStrategyTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private NotificationSender notificationSender;

    @Mock
    private NotificationStrategyResolver notificationStrategyResolver;

    @BeforeEach
    void setUp() {
        NotificationStrategy deliveryCompletionNotificationStrategy = new DeliveryCompletionNotificationStrategy(orderItemRepository, notificationSender);
        NotificationStrategy eventNotificationStrategy = new EventNotificationStrategy(notificationSender);

        notificationStrategyResolver = new NotificationStrategyResolver(
                List.of(deliveryCompletionNotificationStrategy, eventNotificationStrategy)
        );
    }

    @Test
    void eventNotificationStrategy_Handle_Success() {
        //Given
        List<String> phoneNumbers = List.of("01012345678", "01011112222");
        String eventName = "Big Sale Event Test!";
        EventNotificationMessage message = new EventNotificationMessage(phoneNumbers, eventName, "Event Test!");


        NotificationStrategy strategy = notificationStrategyResolver.resolveStrategy("EVENT_TEMPLATE");

        //When
        strategy.handle(message);


        //Then
        verify(notificationSender, times(1)).sendAll(
                eq(phoneNumbers),
                argThat(params -> params.containsKey("eventName") && params.get("eventName").equals(eventName))
        );
    }
}