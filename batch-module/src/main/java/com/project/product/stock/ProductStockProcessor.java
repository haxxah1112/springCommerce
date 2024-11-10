package com.project.product.stock;

import com.project.domain.products.StockLogs;
import com.project.domain.products.repository.StockLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductStockProcessor implements ItemProcessor<Long, Map.Entry<Long, List<StockLogs>>> {
    private final StockLogRepository stockLogRepository;
    @Override
    public Map.Entry<Long, List<StockLogs>> process(Long productId) {
        List<StockLogs> stockLogs = stockLogRepository.findByProductId(productId);

        return new AbstractMap.SimpleEntry<>(productId, stockLogs);
    }
}