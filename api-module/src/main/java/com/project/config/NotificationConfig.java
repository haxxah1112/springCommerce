package com.project.config;

import com.project.executor.NotificationSenderExecutor;
import com.project.notification.sender.NotificationSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {
    @Bean
    public NotificationSender notificationSender() {
        return new NotificationSenderExecutor();
    }

}
