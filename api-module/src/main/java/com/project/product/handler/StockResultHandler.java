package com.project.product.handler;

import com.project.common.kafka.message.StockResultMessage;
import com.project.order.manager.StockResultManager;
import com.project.order.manager.StockResultContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockResultHandler {
    private final StockResultManager stockResultManager;
    public void handle(StockResultMessage resultMessage) {
        StockResultContext context = stockResultManager.getContext(resultMessage.getOrderId());

        context.incrementProcessedCount(resultMessage.isSuccess());

        if (context.isAllProcessed()) {
            if (!context.hasFailed()) {
                // TODO: 주문처리
            }
            stockResultManager.removeContext(resultMessage.getOrderId());
        }

    }
}
