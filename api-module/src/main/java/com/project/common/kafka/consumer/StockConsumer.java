package com.project.common.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.kafka.message.StockMessage;
import com.project.common.kafka.message.StockResultMessage;
import com.project.domain.products.Products;
import com.project.domain.products.repository.ProductRepository;
import com.project.product.handler.StockResultHandler;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class StockConsumer {
    private final StringRedisTemplate redisTemplate;
    private final StockResultHandler stockResultHandler;
    private final ObjectMapper objectMapper;
    private final RedissonClient redissonClient;
    private final ProductRepository productRepository;

    @KafkaListener(topics = "stock-check", groupId = "stock-group")
    public int checkStock(Long productId) {
        String key = "stock:" + productId;
        Products product = productRepository.findById(productId).orElseThrow();
        return product.getStockQuantity();
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
        RLock lock = redissonClient.getLock(key + ":lock");
        boolean isSuccess = false;
        try {
            if (lock.tryLock(10, 2, TimeUnit.SECONDS)) {
                try {
                    Integer currentStock = checkStock(stockMessage.getProductId());
                    if (currentStock >= stockMessage.getQuantity()) {

                        isSuccess = true;
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to acquire lock", e);
        }

        stockResultHandler.handle(new StockResultMessage(stockMessage.getOrderId(),
                stockMessage.getProductId(), stockMessage.getQuantity(), isSuccess));


        return isSuccess;
    }

}
