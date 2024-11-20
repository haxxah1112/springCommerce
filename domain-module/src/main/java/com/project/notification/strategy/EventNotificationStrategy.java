package com.project.notification.strategy;

import com.project.enums.NotificationTemplate;
import com.project.common.message.EventNotificationMessage;
import com.project.common.message.NotificationMessage;
import com.project.notification.sender.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EventNotificationStrategy implements NotificationStrategy {
    private final NotificationSender notificationSender;

    @Override
    public NotificationTemplate getTemplate() {
        return NotificationTemplate.EVENT_TEMPLATE;
    }

    @Override
    public void handle(NotificationMessage notificationMessage) {
        if (!(notificationMessage instanceof EventNotificationMessage message)) {
            throw new IllegalArgumentException("Invalid request type for EventNotificationStrategy");
        }

        notificationSender.sendAll(message.getPhones(), message.toParams());
    }
}
