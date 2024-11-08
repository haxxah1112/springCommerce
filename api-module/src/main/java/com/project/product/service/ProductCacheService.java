package com.project.product.service;

import com.project.domain.products.Products;
import com.project.order.dto.OrderItemDto;
import com.project.product.dto.StockDecrementResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.project.domain.order.QOrderItems.orderItems;

@Service
@RequiredArgsConstructor
public class ProductCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PRODUCT_CACHE_PREFIX = "product:";

    public String getCachedProduct(Long productId) {
        String key = PRODUCT_CACHE_PREFIX + productId;
        return redisTemplate.opsForValue().get(key);
    }

    public void cacheProduct(Products product) {
        String key = PRODUCT_CACHE_PREFIX + product.getId();
        redisTemplate.opsForValue().set(key, String.valueOf(product.getStockQuantity()), 30, TimeUnit.MINUTES);
    }

    public boolean decrementStock(Long productId, int quantity) {
        String key = PRODUCT_CACHE_PREFIX + productId;

        Long stock = redisTemplate.opsForValue().increment(key, -quantity);

        if (stock != null && stock < 0) {
            redisTemplate.opsForValue().increment(key, quantity);
            return false;
        }

        return true;
    }

    public List<StockDecrementResult> decrementStocks(List<OrderItemDto> orderItems) {
        return redisTemplate.execute(new SessionCallback<List<StockDecrementResult>>() {
            @Override
            public List<StockDecrementResult> execute(RedisOperations operations) {
                List<StockDecrementResult> results = new ArrayList<>();
                Map<String, OrderItemDto> keyToOrderItem = new HashMap<>();

                try {
                    for (OrderItemDto item : orderItems) {
                        String key = PRODUCT_CACHE_PREFIX + item.getProductId();
                        keyToOrderItem.put(key, item);
                        operations.watch(key);
                    }

                    boolean allStocksSufficient = true;
                    for (Map.Entry<String, OrderItemDto> entry : keyToOrderItem.entrySet()) {
                        String stockValue = (String) operations.opsForValue().get(entry.getKey());
                        Long currentStock = stockValue != null ? Long.parseLong(stockValue) : null;

                        if (currentStock == null || currentStock < entry.getValue().getQuantity()) {
                            allStocksSufficient = false;
                            break;
                        }
                    }

                    if (!allStocksSufficient) {
                        throw new RuntimeException("empty stock");
                    }

                    operations.multi();

                    for (Map.Entry<String, OrderItemDto> entry : keyToOrderItem.entrySet()) {
                        operations.opsForValue().increment(entry.getKey(), -entry.getValue().getQuantity());
                    }

                    List<Object> execResult = operations.exec();

                    if (execResult == null || execResult.isEmpty()) {
                        return results;
                    }

                    int i = 0;
                    for (Map.Entry<String, OrderItemDto> entry : keyToOrderItem.entrySet()) {
                        Long newStock = (Long) execResult.get(i++);
                        OrderItemDto item = entry.getValue();

                        boolean success = newStock != null && newStock >= 0;
                        results.add(new StockDecrementResult(item.getProductId(), item.getQuantity(), success));

                        if (!success) {
                            operations.opsForValue().increment(entry.getKey(), item.getQuantity());
                        }
                    }

                    return results;

                } catch (Exception e) {
                    rollbackStocks(results);
                    throw e;
                }
            }
        });
    }

    public void rollbackStocks(List<StockDecrementResult> stockDecrementResults) {
        for (StockDecrementResult result : stockDecrementResults) {
            if (result.isSuccess()) {
                String key = PRODUCT_CACHE_PREFIX + result.getProductId();
                redisTemplate.opsForValue().increment(key, result.getQuantity());
            }
        }
    }
}
