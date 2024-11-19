package com.project.order.service;

import com.project.domain.order.OrderItems;
import com.project.domain.order.Orders;
import com.project.domain.products.Products;
import com.project.domain.users.Users;
import com.project.order.dto.OrderItemDto;
import com.project.order.dto.OrderResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderConverter {

    public Orders convertRequestDtoToOrderEntity(Users user) {
        return Orders.builder()
                .user(user)
                .build();
    }

    public OrderItems convertOrderItemDtoToOrderItemEntity(OrderItemDto productDto, Products product) {
        return OrderItems.builder()
                .product(product)
                .quantity(productDto.getQuantity())
                .price(product.getPrice() * productDto.getQuantity())
                .build();
    }

    public OrderResponseDto convertOrderEntityToOrderResponseDto(Orders order) {
        List<OrderItemDto> items = order.getOrderItems().stream()
                .map(orderItem -> OrderItemDto.builder()
                        .productId(orderItem.getProduct().getId())
                        .quantity(orderItem.getQuantity())
                        .build())
                .toList();

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .items(items)
                .totalAmount(order.getTotalPrice())
                .orderStatus(order.getStatus().name())
                .build();
    }
}
