package com.project.common.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockMessage {
    private Long orderId;
    private Long productId;
    private int quantity;
    private int price;
}
