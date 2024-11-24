package com.project.event.stock;

import com.project.common.message.DecrementProductStockMessage;

import java.util.List;

public interface StockSender {
    void sendDecrementStockMessages(List<DecrementProductStockMessage> messages);

}
