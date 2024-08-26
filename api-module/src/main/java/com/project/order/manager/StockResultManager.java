package com.project.order.manager;

import com.project.order.dto.OrderItemDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StockResultManager {
    private final ConcurrentHashMap<Long, StockResultContext> orderResults = new ConcurrentHashMap<>();

    public void addContext(Long orderId, StockResultContext context) {
        orderResults.put(orderId, context);
    }

    public StockResultContext getContext(Long orderId) {
        return orderResults.get(orderId);
    }

    public void removeContext(Long orderId) {
        orderResults.remove(orderId);
    }
}
