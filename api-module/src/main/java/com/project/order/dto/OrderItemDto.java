package com.project.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemDto {
    private Long productId;
    private int quantity;
    private int price;
    private boolean isSuccess;
}
