package com.project.common.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.kafka.message.StockMessage;
import com.project.common.kafka.message.StockResultMessage;
import com.project.common.util.JsonUtil;
import com.project.domain.products.Products;
import com.project.domain.products.repository.ProductRepository;
import com.project.order.dto.OrderItemDto;
import com.project.product.handler.StockResultHandler;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class StockConsumer {
    private final StockResultHandler stockResultHandler;
    private final RedissonClient redissonClient;
    private final ProductRepository productRepository;


    @KafkaListener(topics = "stock-check", groupId = "stock-group")
    public int checkStock(Long productId) {
        String key = "stock:" + productId;
        Products product = productRepository.findById(productId).orElseThrow();
        return product.getStockQuantity();
    }

    public int checkPrice(Long productId) {
        Products product = productRepository.findById(productId).orElseThrow();
        return product.getPrice();
    }

    @KafkaListener(topics = "stock-decrement", groupId = "stock-group")
    public boolean decrementStock(String message) {
        StockMessage stockMessage = JsonUtil.deserialize(message, StockMessage.class);

        String key = "stock:" + stockMessage.getProductId();
        RLock lock = redissonClient.getLock(key + ":lock");
        boolean isSuccess = false;
        try {
            if (lock.tryLock(10, 2, TimeUnit.SECONDS)) {
                try {
                    Integer currentStock = checkStock(stockMessage.getProductId());
                    int currentPrice = checkPrice(stockMessage.getProductId());

                    if (currentStock >= stockMessage.getQuantity() && currentPrice == stockMessage.getPrice()) {

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

    @KafkaListener(topics = "stock-rollback", groupId = "rollback-group")
    public void rollbackStock(String item){
        OrderItemDto orderItem = JsonUtil.deserialize(item, OrderItemDto.class);
        String key = "stock:" + orderItem.getProductId();
        RLock lock = redissonClient.getLock(key + ":lock");

        try {
            if (lock.tryLock(10, 2, TimeUnit.SECONDS)) {
                try {
                    Products product = productRepository.findById(orderItem.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    product.increaseStock(orderItem.getQuantity());
                    productRepository.save(product);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to acquire lock for rollback", e);
        }
    }

}
