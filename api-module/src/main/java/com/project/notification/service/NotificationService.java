package com.project.notification.service;

import com.project.notification.dto.NotificationSendRequestDto;

public interface NotificationService {
    void sendNotification(NotificationSendRequestDto request);
}
