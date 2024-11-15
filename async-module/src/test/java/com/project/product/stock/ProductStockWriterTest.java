package com.project.product.stock;

import com.project.BrandFixture;
import com.project.ProductFixture;
import com.project.StockLogFixture;
import com.project.UserFixture;
import com.project.domain.brands.Brands;
import com.project.domain.products.Categories;
import com.project.domain.products.Products;
import com.project.domain.products.StockLogStatus;
import com.project.domain.products.StockLogs;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.products.repository.StockLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductStockWriterTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockLogRepository stockLogRepository;

    @InjectMocks
    private ProductStockWriter productStockWriter;

    private Products product1;
    private Products product2;

    @BeforeEach
    void setUp() {
        Brands brand = BrandFixture.createBrand("testBrand", UserFixture.createDefaultUser());

        product1 = ProductFixture.createProduct("product1", brand, 2000, Categories.TOP, 100);
        product2 = ProductFixture.createProduct("product2", brand, 10000, Categories.PANTS, 100);
    }

    @Test
    void productStockWriterTest() {
        //Given
        StockLogs stockLog1 = StockLogFixture.createStockLog(1L, 3, StockLogStatus.FAIL);
        StockLogs stockLog2 = StockLogFixture.createStockLog(2L, 2, StockLogStatus.FAIL);
        StockLogs stockLog3 = StockLogFixture.createStockLog(1L, 1, StockLogStatus.FAIL);

        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(java.util.Optional.of(product2));
        when(stockLogRepository.save(any(StockLogs.class))).thenAnswer(stockLog -> stockLog.getArgument(0));


        Map.Entry<Long, List<StockLogs>> entry1 = new AbstractMap.SimpleEntry<>(1L, List.of(stockLog1, stockLog3));
        Map.Entry<Long, List<StockLogs>> entry2 = new AbstractMap.SimpleEntry<>(2L, List.of(stockLog2));

        //When
        productStockWriter.write(Chunk.of(entry1, entry2));

        //Then
        assertEquals(96, product1.getStockQuantity());
        assertEquals(98, product2.getStockQuantity());
        assertEquals(StockLogStatus.COMPLETED, stockLog1.getStatus());

      }
}