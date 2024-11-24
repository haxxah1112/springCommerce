package com.project.enums;

import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;

public enum NotificationTemplate {
    DELIVERY_COMPLETION_TEMPLATE,
    EVENT_TEMPLATE;

    public static NotificationTemplate fromString(String template) {
        try {
            return NotificationTemplate.valueOf(template);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(CustomError.NOT_FOUND, "No strategy found for key: " + template);
        }
    }
}
