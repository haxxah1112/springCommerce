package com.project;

import com.project.domain.products.StockLogStatus;
import com.project.domain.products.StockLogs;

public class StockLogFixture {
    public static StockLogs createStockLog(Long productId, int quantity, StockLogStatus status) {
        return StockLogs.builder()
                .orderId(1L)
                .productId(productId)
                .quantity(quantity)
                .status(status)
                .build();
    }
}
