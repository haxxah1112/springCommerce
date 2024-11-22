package com.project.listener;

import com.project.common.message.NotificationMessage;
import com.project.notification.strategy.NotificationStrategy;
import com.project.notification.strategy.NotificationStrategyResolver;
import com.project.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationStrategyResolver notificationStrategyResolver;

    @KafkaListener(topics = "notification-requests", groupId = "notification-group")
    public void requestNotification(String message) {
        NotificationMessage request = JsonUtil.deserialize(message, NotificationMessage.class);

        NotificationStrategy notificationStrategy = notificationStrategyResolver.resolveStrategy(request.getTemplate());
        notificationStrategy.handle(request);
    }
}
