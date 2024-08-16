package com.project.product.handler;

import com.project.common.kafka.message.StockResultMessage;
import com.project.order.manager.StockResultContext;
import com.project.order.manager.StockResultManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockResultHandlerTest {
    @InjectMocks
    private StockResultHandler stockResultHandler;

    @Mock
    private StockResultManager stockResultManager;

    @Test
    @DisplayName("재고 결과 성공 처리 - 모든 상품을 검사하여 성공을 반환한다")
    public void handleStockResult_Success() {
        //Given
        Long orderId = 1L;
        StockResultContext context = new StockResultContext(2);
        when(stockResultManager.getContext(orderId)).thenReturn(context);

        StockResultMessage firstResultMessage = new StockResultMessage(orderId, 1L, 10, true);
        StockResultMessage secondResultMessage = new StockResultMessage(orderId, 2L, 5, true);

        //When
        stockResultHandler.handle(firstResultMessage);
        stockResultHandler.handle(secondResultMessage);
        
        //Then
        assertTrue(context.isAllProcessed());
        assertFalse(context.hasFailed());
    }

    @Test
    @DisplayName("재고 결과 실패 처리 - 모든 상품을 검사하여 하나의 상품이라도 재고 부족 시 실패를 반환한다")
    public void handleStockResult_Fail() {
        //Given
        Long orderId = 1L;
        StockResultContext context = new StockResultContext(2);
        when(stockResultManager.getContext(orderId)).thenReturn(context);

        StockResultMessage firstResultMessage = new StockResultMessage(orderId, 1L, 10, true);
        StockResultMessage secondResultMessage = new StockResultMessage(orderId, 2L, 5, false);

        //When
        stockResultHandler.handle(firstResultMessage);
        stockResultHandler.handle(secondResultMessage);

        //Then
        assertTrue(context.isAllProcessed());
        assertTrue(context.hasFailed());
    }
}