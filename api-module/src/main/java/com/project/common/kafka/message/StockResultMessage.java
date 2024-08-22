package com.project.common.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockResultMessage {
    private Long orderId;
    private Long productId;
    private int quantity;
    private boolean success;
}
