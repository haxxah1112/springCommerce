package com.project.common.kafka.producer;

import com.project.common.Message;
import com.project.common.kafka.message.StockMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class StockProducer {
    private final KafkaTemplate<String, Message> kafkaTemplate;

    public CompletableFuture<SendResult<String, Message>> sendOrderEvent(StockMessage message) {
        return kafkaTemplate.send("stock-decrement", message);
    }
}
