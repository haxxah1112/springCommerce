package com.project.notification.strategy;

import com.project.enums.NotificationTemplate;
import com.project.common.message.NotificationMessage;

public interface NotificationStrategy {
    NotificationTemplate getTemplate();
    void handle(NotificationMessage request);

}
