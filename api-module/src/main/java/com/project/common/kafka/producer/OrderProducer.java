package com.project.common.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendRollbackOrderEvent(Long orderId) {
        kafkaTemplate.send("order-rollback", String.valueOf(orderId));
    }

}
