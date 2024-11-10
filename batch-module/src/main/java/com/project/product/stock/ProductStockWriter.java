package com.project.product.stock;

import com.project.domain.products.Products;
import com.project.domain.products.StockLogs;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.products.repository.StockLogRepository;
import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductStockWriter implements ItemWriter<Map.Entry<Long, List<StockLogs>>> {
    private final ProductRepository productRepository;
    private final StockLogRepository stockLogRepository;

    @Override
    @Transactional
    public void write(Chunk<? extends Map.Entry<Long, List<StockLogs>>> items) {
        for (Map.Entry<Long, List<StockLogs>> entry : items) {
            Long productId = entry.getKey();
            List<StockLogs> stockLogs = entry.getValue();

            int totalQuantityToDeduct = stockLogs.stream()
                    .mapToInt(StockLogs::getQuantity)
                    .sum();

            Products product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));
            product.decreaseStock(totalQuantityToDeduct);
            productRepository.save(product);

            stockLogs.forEach(stockLog -> {
                stockLog.complete();
                stockLogRepository.save(stockLog);
            });
        }
    }
}