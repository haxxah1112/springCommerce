package com.project.product.handler;

import com.project.common.kafka.message.StockResultMessage;
import com.project.common.kafka.producer.OrderProducer;
import com.project.common.kafka.producer.StockProducer;
import com.project.order.dto.OrderItemDto;
import com.project.order.manager.StockResultManager;
import com.project.order.manager.StockResultContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockResultHandler {
    private final StockResultManager stockResultManager;
    private final StockProducer stockProducer;
    private final OrderProducer orderProducer;

    public void handle(StockResultMessage resultMessage) {
        StockResultContext context = stockResultManager.getContext(resultMessage.getOrderId());
        context.updateItemStatus(resultMessage.getProductId(), resultMessage.isSuccess());

        context.incrementProcessedCount(resultMessage.isSuccess());

        if (context.isAllProcessed()) {
            if (!context.hasFailed()) {
                // TODO: 주문처리
            } else {
                orderProducer.sendRollbackOrderEvent(resultMessage.getOrderId());
                for (OrderItemDto item : context.getItems()) {
                    stockProducer.sendRollbackStock(item);
                }
            }
            stockResultManager.removeContext(resultMessage.getOrderId());
        }

    }

}
