package com.project.common.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.kafka.message.StockMessage;
import com.project.common.util.JsonUtil;
import com.project.order.dto.OrderItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class StockProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendOrderEvent(StockMessage stockMessage) {
        String message = JsonUtil.serialize(stockMessage);
        kafkaTemplate.send("stock-decrement", String.valueOf(stockMessage.getOrderId()), message);
    }

    public void sendRollbackStock(OrderItemDto item) {
        String message = JsonUtil.serialize(item);
        kafkaTemplate.send("stock-rollback", message);
    }
}
