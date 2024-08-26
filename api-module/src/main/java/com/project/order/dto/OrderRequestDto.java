package com.project.order.dto;

import com.project.address.dto.AddressRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    private Long userId;
    private List<OrderItemDto> items;
    private Long addressId;
    private String paymentMethod;
}
