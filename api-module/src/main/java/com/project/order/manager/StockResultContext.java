package com.project.order.manager;

import java.util.concurrent.atomic.AtomicBoolean;

public class StockResultContext {
    private final int totalItems;
    private final AtomicBoolean failed = new AtomicBoolean(false);
    private int processedCount = 0;

    public StockResultContext(int totalItems) {
        this.totalItems = totalItems;
    }

    public synchronized void incrementProcessedCount() {
        processedCount++;
    }

    public synchronized boolean isAllProcessed() {
        return processedCount == totalItems;
    }

    public boolean hasFailed() {
        return failed.get();
    }

}
