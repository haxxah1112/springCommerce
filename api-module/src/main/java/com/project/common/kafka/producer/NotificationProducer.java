package com.project.common.kafka.producer;

import com.project.util.JsonUtil;
import com.project.common.message.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendNotificationEvents(NotificationMessage message) {
        String serializedMessage = JsonUtil.serialize(message);
        kafkaTemplate.send("notification-requests", serializedMessage);
    }

}
