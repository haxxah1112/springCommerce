package com.project.order.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class OrderRequestDto {
    private Long userId;
    private List<OrderItemDto> items;
    private Long addressId;
    private String paymentMethod;
}
