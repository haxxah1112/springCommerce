package com.project.common.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.kafka.message.StockMessage;
import com.project.order.dto.OrderItemDto;
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

    public void sendRollbackStock(OrderItemDto item) {
        try {
            String message = objectMapper.writeValueAsString(item);
            kafkaTemplate.send("stock-rollback", message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
