package com.project.listener;

import com.project.common.message.DecrementProductStockMessage;
import com.project.util.JsonUtil;
import com.project.domain.products.Products;
import com.project.domain.products.StockLogs;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.products.repository.StockLogRepository;
import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class StockConsumer {
    private final RedissonClient redissonClient;
    private final ProductRepository productRepository;
    private final StockLogRepository stockLogRepository;

    @KafkaListener(topics = "stock-decrement", groupId = "stock-group")
    public void decrementProductStock(String decrementProductStockMessage) {
        DecrementProductStockMessage message = JsonUtil.deserialize(decrementProductStockMessage, DecrementProductStockMessage.class);
        RLock lock = redissonClient.getLock("product-lock:" + message.getProductId());

        try {
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                try {
                    Products product = productRepository.findById(message.getProductId())
                            .orElseThrow(()-> new NotFoundException(CustomError.NOT_FOUND));
                    product.decreaseStock(message.getQuantity());
                    productRepository.save(product);

                    StockLogs stockLog = stockLogRepository.findById(message.getStockLogId())
                            .orElseThrow(()-> new NotFoundException(CustomError.NOT_FOUND));
                    stockLog.complete();
                    stockLogRepository.save(stockLog);

                } finally {
                    lock.unlock();
                }
            }
        } catch (Exception e) {
            StockLogs stockLog = stockLogRepository.findById(message.getStockLogId())
                    .orElseThrow(()-> new NotFoundException(CustomError.NOT_FOUND));
            stockLog.fail(e.getMessage());
            stockLogRepository.save(stockLog);
        }
    }
}
