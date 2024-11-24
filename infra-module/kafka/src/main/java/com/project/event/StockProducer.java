package com.project.event;

import com.project.common.message.DecrementProductStockMessage;
import com.project.event.stock.StockSender;
import com.project.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StockProducer implements StockSender {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendDecrementStockMessages(List<DecrementProductStockMessage> messages) {
        kafkaTemplate.executeInTransaction(operations -> {
            for (DecrementProductStockMessage message : messages) {
                String serializedMessage = JsonUtil.serialize(message);
                operations.send("stock-decrement", serializedMessage);
            }
            return true;
        });
    }
}
