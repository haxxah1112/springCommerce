package com.project.common.kafka.message;

import com.project.common.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockResultMessage implements Message {
    private Long orderId;
    private Long productId;
    private int quantity;
    private boolean success;
}
