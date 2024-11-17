package com.project.common.kafka.producer;

import com.project.common.message.DecrementProductStockMessage;
import com.project.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class StockProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String transactionPrefix = "tx-stock-";


    public void sendProductDecrementEvents(List<DecrementProductStockMessage> messages) {
        kafkaTemplate.setTransactionIdPrefix(transactionPrefix + UUID.randomUUID());

        kafkaTemplate.executeInTransaction(operations -> {
            for (DecrementProductStockMessage message : messages) {
                String serializedMessage = JsonUtil.serialize(message);
                operations.send("stock-decrement", serializedMessage);
            }
            return true;
        });
    }
}
