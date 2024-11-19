package com.project.order.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long productId;
    private int quantity;
    private int price;
}
