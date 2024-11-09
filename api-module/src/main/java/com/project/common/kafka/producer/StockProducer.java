package com.project.common.kafka.producer;

import com.project.common.kafka.message.DecrementProductStockMessage;
import com.project.common.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class StockProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendProductDecrementEvent(DecrementProductStockMessage decrementProductStockMessage) {
        String message = JsonUtil.serialize(decrementProductStockMessage);
        kafkaTemplate.send("stock-decrement", message);
    }
}
