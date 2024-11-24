package com.project.order.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderResponseDto {
    private Long orderId;
    private Long userId;
    private List<OrderItemDto> items;
    private int totalAmount;
    private String orderStatus;
}
