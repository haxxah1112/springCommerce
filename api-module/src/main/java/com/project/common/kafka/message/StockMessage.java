package com.project.common.kafka.message;

import com.project.common.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class StockMessage implements Message {
    private Long orderId;
    private Long productId;
    private int quantity;
}
