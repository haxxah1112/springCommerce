package com.project.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationSendRequestDto {
    private String message;
    private String notificationType;
}
