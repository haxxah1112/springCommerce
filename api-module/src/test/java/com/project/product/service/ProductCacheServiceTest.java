package com.project.product.service;

import com.project.ProductFixture;
import com.project.domain.products.Products;
import com.project.order.dto.OrderItemDto;
import com.project.product.dto.StockDecrementResult;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductCacheServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private RedisOperations<String, String> redisOperations;

    private ProductCacheService productCacheService;

    @BeforeEach
    void setUp() {
        productCacheService = new ProductCacheService(redisTemplate);
    }

    @Test
    @DisplayName("캐시에서 상품 정보를 정상적으로 조회한다")
    void testGetCachedProduct() {
        //Given
        String productId = "123";
        String stock = "100";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("product:" + productId)).thenReturn(stock);

        //When
        String result = productCacheService.getCachedProduct(Long.parseLong(productId));

        //Then
        assertEquals(stock, result);
        verify(valueOperations).get("product:" + productId);
    }

    @Test
    @DisplayName("상품 정보를 캐시에 저장한다")
    void testCacheProduct() {
        //Given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Products product = ProductFixture.createDefaultProduct(1L);

        //When
        productCacheService.cacheProduct(product);

        //Then
        verify(valueOperations).set(
                eq("product:1"),
                eq("500"),
                eq(30L),
                eq(TimeUnit.MINUTES)
        );
    }

    @Test
    @DisplayName("재고 감소가 정상적으로 동작한다")
    void testDecrementStock_Successful() {
        //Given
        Long productId = 789L;
        int quantity = 10;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("product:789", -quantity)).thenReturn(40L);

        //When
        boolean result = productCacheService.decrementStock(productId, quantity);

        //Then
        assertTrue(result);
    }

    @Test
    @DisplayName("재고가 부족할 경우 재고 감소가 실패한다")
    void testDecrementStock_InsufficientStock() {
        //Given
        Long productId = 789L;
        int quantity = 10;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("product:789", -quantity)).thenReturn(-5L);

        //When
        boolean result = productCacheService.decrementStock(productId, quantity);

        //Then
        assertFalse(result);
    }

    @Test
    @DisplayName("여러 상품의 재고가 모두 정상적으로 감소된다")
    void testDecrementStocks_AllSuccessful() {
        //Given
        List<OrderItemDto> orderItems = List.of(
                new OrderItemDto(1L, 10, 3000),
                new OrderItemDto(2L, 20, 2000)
        );

        when(redisTemplate.execute(any(SessionCallback.class))).thenAnswer(invocation -> {
            SessionCallback<?> callback = invocation.getArgument(0);
            RedisOperations operations = mock(RedisOperations.class);
            ValueOperations valueOps = mock(ValueOperations.class);

            when(operations.opsForValue()).thenReturn(valueOps);

            when(valueOps.get("product:1")).thenReturn("20");
            when(valueOps.get("product:2")).thenReturn("45");

            doNothing().when(operations).multi();
            when(operations.exec()).thenReturn(List.of(10L, 25L));

            return callback.execute(operations);
        });

        //When
        List<StockDecrementResult> results = productCacheService.decrementStocks(orderItems);

        //Then
        assertThat(results).isNotNull();
        assertThat(results).hasSize(2);

        assertThat(results.get(0).getProductId()).isEqualTo(1L);
        assertThat(results.get(0).getQuantity()).isEqualTo(10);
        assertThat(results.get(0).isSuccess()).isTrue();

        assertThat(results.get(1).getProductId()).isEqualTo(2L);
        assertThat(results.get(1).getQuantity()).isEqualTo(20);
        assertThat(results.get(1).isSuccess()).isTrue();
    }

    @Test
    @DisplayName("재고 감소가 실패한 상품에 대해 롤백 처리를 한다")
    void testRollbackStocks() {
        //Given
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        List<StockDecrementResult> results = List.of(
                new StockDecrementResult(1L, 10, true),
                new StockDecrementResult(2L, 20, false)
        );

        //When
        productCacheService.rollbackStocks(results);

        //Then
        verify(valueOperations).increment("product:1", 10);
        verifyNoMoreInteractions(valueOperations);
    }


}