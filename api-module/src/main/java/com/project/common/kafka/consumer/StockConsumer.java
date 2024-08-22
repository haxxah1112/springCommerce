package com.project.common.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.kafka.message.StockMessage;
import com.project.common.kafka.message.StockResultMessage;
import com.project.product.handler.StockResultHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StockConsumer {
    private final StringRedisTemplate redisTemplate;
    private final StockResultHandler stockResultHandler;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "stock-check", groupId = "stock-group")
    public int checkStock(Long productId) {
        String key = "stock:" + productId;
        String stockStr = redisTemplate.opsForValue().get(key);
        return stockStr != null ? Integer.parseInt(stockStr) : 0;
    }

    @KafkaListener(topics = "stock-decrement", groupId = "stock-group")
    public boolean decrementStock(String message) {
        StockMessage stockMessage;
        try {
            stockMessage = objectMapper.readValue(message, StockMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize StockMessage", e);
        }

        String key = "stock:" + stockMessage.getProductId();
        boolean isSuccess = redisTemplate.execute(new SessionCallback<Boolean>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                operations.watch(key);
                Integer currentStock = checkStock(stockMessage.getProductId());
                if (currentStock >= stockMessage.getQuantity()) {
                    operations.multi();
                    operations.opsForValue().set(key, String.valueOf(currentStock - stockMessage.getQuantity()));
                    List<Object> result = operations.exec();
                    return !result.isEmpty();
                }
                return false;
            }
        });

        stockResultHandler.handle(new StockResultMessage(stockMessage.getOrderId(),
                stockMessage.getProductId(), stockMessage.getQuantity(), isSuccess));


        return isSuccess;
    }

}
