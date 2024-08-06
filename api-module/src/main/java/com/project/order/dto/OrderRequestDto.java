package com.project.order.dto;

import com.project.address.dto.AddressRequestDto;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto {
    private Long userId;
    private List<OrderItemDto> items;
    private Long addressId;
    private String paymentMethod;
}
