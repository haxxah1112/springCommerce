package com.project.common.kafka.producer;

import com.project.common.Message;
import com.project.common.kafka.message.StockMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockProducer {
    private final KafkaTemplate<String, Message> kafkaTemplate;

    public void sendOrderEvent(StockMessage message) {
        kafkaTemplate.send("stock-decrement", message);
    }
}
