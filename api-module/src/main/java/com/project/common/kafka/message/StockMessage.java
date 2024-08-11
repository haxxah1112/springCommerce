package com.project.common.kafka.message;

import com.project.common.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class StockMessage implements Message {
    private Long productId;
    private int quantity;
}
