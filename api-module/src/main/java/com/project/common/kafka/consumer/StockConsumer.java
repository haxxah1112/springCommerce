package com.project.common.kafka.consumer;

import com.project.common.Message;
import com.project.common.kafka.message.StockMessage;
import com.project.common.kafka.message.StockResultMessage;
import com.project.product.handler.StockResultHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StockConsumer {
    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final StockResultHandler stockResultHandler;

    @KafkaListener(topics = "stock-check", groupId = "stock-group")
    public int checkStock(Long productId) {
        String key = "stock:" + productId;
        String stockStr = redisTemplate.opsForValue().get(key);
        return stockStr != null ? Integer.parseInt(stockStr) : 0;
    }

    @KafkaListener(topics = "stock-decrement", groupId = "stock-group")
    public boolean decrementStock(StockMessage message) {
        String key = "stock:" + message.getProductId();
        boolean isSuccess = redisTemplate.execute(new SessionCallback<Boolean>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                operations.watch(key);
                Integer currentStock = checkStock(message.getProductId());
                if (currentStock >= message.getQuantity()) {
                    operations.multi();
                    operations.opsForValue().set(key, String.valueOf(currentStock - message.getQuantity()));
                    List<Object> result = operations.exec();
                    return !result.isEmpty();
                }
                return false;
            }
        });

        stockResultHandler.handle(new StockResultMessage(message.getOrderId(),
                message.getProductId(), message.getQuantity(), isSuccess));


        return isSuccess;
    }

}
