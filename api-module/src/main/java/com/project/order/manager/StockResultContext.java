package com.project.order.manager;

import com.project.common.kafka.message.StockResultMessage;
import com.project.order.dto.OrderItemDto;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class StockResultContext {
    private final int totalItems;
    private final AtomicBoolean failed = new AtomicBoolean(false);
    private int processedCount = 0;
    private List<OrderItemDto> items;

    public StockResultContext(int totalItems, List<OrderItemDto> items) {
        this.totalItems = totalItems;
        this.items = items;
    }

    public synchronized void incrementProcessedCount(boolean success) {
        if (!success) {
            setFailed();
        }
        processedCount++;
    }

    public synchronized boolean isAllProcessed() {
        return processedCount == totalItems;
    }

    public boolean hasFailed() {
        return failed.get();
    }

    public boolean setFailed() {
        return failed.compareAndSet(false, true);
    }

    public void updateItemStatus(Long productId, boolean isSuccess) {
        for (OrderItemDto item : items) {
            if (item.getProductId().equals(productId)) {
                item.setSuccess(isSuccess);
                break;
            }
        }
    }
}
