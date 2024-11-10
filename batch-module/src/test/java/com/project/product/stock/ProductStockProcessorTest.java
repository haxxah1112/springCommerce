package com.project.product.stock;

import com.project.domain.products.StockLogs;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductStockProcessorTest {
    @InjectMocks
    private ProductStockProcessor productStockProcessor;

    @Mock
    private StockLogs stockLog;

    @Test
    void productStockProcessorTest() {
        //Given
        Long productId = 1L;
        when(stockLog.getProductId()).thenReturn(productId);

        //When
        Map.Entry<Long, List<StockLogs>> result = productStockProcessor.process(productId);

        //Then
        assertEquals(productId, result.getKey());
        assertEquals(1, result.getValue().size());
        assertEquals(stockLog, result.getValue().get(0));

    }
}