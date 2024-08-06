package com.project.order.service;

import com.project.domain.order.OrderItems;
import com.project.domain.order.Orders;
import com.project.domain.products.Products;
import com.project.domain.users.Users;
import com.project.order.dto.OrderItemDto;
import com.project.order.dto.OrderRequestDto;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {

    public Orders convertRequestDtoToOrderEntity(OrderRequestDto request, Users user) {
        return Orders.builder()
                .user(user)
                .build();
    }

    public OrderItems convertOrderItemDtoToOrderItemEntity(OrderItemDto productDto, Orders order, Products product) {
        return OrderItems.builder()
                .order(order)
                .product(product)
                .quantity(productDto.getQuantity())
                .price(product.getPrice() * productDto.getQuantity())
                .build();
    }
}
