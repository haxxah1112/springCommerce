package com.project.event.stock;

import com.project.common.message.DecrementProductStockMessage;
import com.project.domain.products.StockLogStatus;
import com.project.domain.products.StockLogs;
import com.project.domain.products.repository.StockLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StockLogEventListener {
    private final StockSender stockSender;
    private final StockLogRepository stockLogRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleStockLogEvent(StockLogEvent event) {
        List<StockLogs> stockLogs = event.getStockDecrementResults().stream()
                .map(result -> StockLogs.builder()
                        .orderId(event.getOrderId())
                        .productId(result.getProductId())
                        .quantity(result.getQuantity())
                        .status(result.isSuccess() ? StockLogStatus.PENDING : StockLogStatus.FAIL)
                        .build())
                .toList();

        stockLogRepository.saveAll(stockLogs);

        List<DecrementProductStockMessage> messages = stockLogs.stream()
                .map(result -> new DecrementProductStockMessage(
                        result.getProductId(),
                        result.getQuantity(),
                        result.getId()
                ))
                .toList();

        stockSender.sendDecrementStockMessages(messages);
    }
}
