package com.project.product.stock;

import com.project.domain.products.StockLogs;
import com.project.domain.products.repository.StockLogQueryRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StockLogReader implements ItemReader<Long> {
    private final StockLogQueryRepositoryImpl stockLogQueryRepository;
    private Iterator<StockLogs> stockLogIterator = Collections.emptyIterator();

    @Override
    public Long read() throws Exception {
        if (!stockLogIterator.hasNext()) {
            List<StockLogs> stockLogs = stockLogQueryRepository.findFailedAndPendingLogs();
            stockLogIterator = stockLogs.iterator();
        }

        return stockLogIterator.hasNext() ? stockLogIterator.next().getProductId() : null;
    }
}