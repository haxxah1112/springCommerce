package com.project.product.handler;

import com.project.common.kafka.message.StockResultMessage;
import com.project.order.manager.StockResultManager;
import com.project.order.manager.StockResultContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockResultHandler {
    private final StockResultManager orderContextManager;
    public void handle(StockResultMessage resultMessage) {
        StockResultContext context = orderContextManager.getContext(resultMessage.getOrderId());

        context.incrementProcessedCount();

        if (context.isAllProcessed()) {
            orderContextManager.removeContext(resultMessage.getOrderId());
        }
    }
}
