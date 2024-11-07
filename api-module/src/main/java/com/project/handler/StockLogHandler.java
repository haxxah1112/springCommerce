package com.project.handler;

import com.project.domain.order.Orders;
import com.project.domain.products.StockLogStatus;
import com.project.domain.products.StockLogs;
import com.project.domain.products.repository.StockLogRepository;
import com.project.product.dto.StockDecrementResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StockLogHandler {
    private final StockLogRepository stockLogRepository;
    public List<StockLogs> saveStockLogs(List<StockDecrementResult> stockDecrementResults, Orders order) {
        List<StockLogs> stockLogs = stockDecrementResults.stream()
                .map(result -> StockLogs.builder()
                        .orderId(order.getId())
                        .productId(result.getProductId())
                        .quantity(result.getQuantity())
                        .status(result.isSuccess() ? StockLogStatus.PENDING : StockLogStatus.FAIL)
                        .build())
                .collect(Collectors.toList());

        return stockLogRepository.saveAll(stockLogs);
    }
}
