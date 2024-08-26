package com.project.common.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.kafka.message.StockMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendOrderEvent(StockMessage stockMessage) {
        try {
            String message = objectMapper.writeValueAsString(stockMessage);
            kafkaTemplate.send("stock-decrement", String.valueOf(stockMessage.getOrderId()), message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendRollbackOrderEvent(Long orderId) {
         kafkaTemplate.send("order-rollback", String.valueOf(orderId));
    }

}
