package com.project.common.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockConsumer {
    private final StringRedisTemplate redisTemplate;

    @KafkaListener(topics = "stock-check", groupId = "stock-group")
    public int checkStock(Long productId) {
        String key = "stock:" + productId;
        String stockStr = redisTemplate.opsForValue().get(key);
        return stockStr != null ? Integer.parseInt(stockStr) : 0;
    }

    @KafkaListener(topics = "stock-decrement", groupId = "stock-group")
    public boolean decrementStock(Long productId, int quantity) {
        String key = "stock:" + productId;
        return redisTemplate.execute(new SessionCallback<Boolean>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                operations.watch(key);
                Integer currentStock = checkStock(productId);
                if (currentStock >= quantity) {
                    operations.multi();
                    operations.opsForValue().set(key, String.valueOf(currentStock - quantity));
                    List<Object> result = operations.exec();
                    return !result.isEmpty();
                }
                return false;
            }
        });
    }

}
