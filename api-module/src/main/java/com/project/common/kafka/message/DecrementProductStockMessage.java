package com.project.common.kafka.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class DecrementProductStockMessage {
    private Long productId;
    private int quantity;
    private Long stockLogId;
}
