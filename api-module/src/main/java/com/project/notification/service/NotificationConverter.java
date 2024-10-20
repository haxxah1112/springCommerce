package com.project.notification.service;

import com.project.domain.notification.Notifications;
import com.project.domain.users.Users;
import com.project.notification.dto.NotificationSendRequestDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter {
    public Notifications convertRequestToEntity(Users user, NotificationSendRequestDto request) {
        return Notifications.builder()
                .user(user)
                .message(request.getMessage())
                .isRead(false)
                .build();
    }
}
