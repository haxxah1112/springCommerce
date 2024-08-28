package com.project.fixture;

import com.project.domain.order.OrderItems;
import com.project.domain.order.OrderStatus;
import com.project.domain.order.Orders;
import com.project.domain.products.Products;
import com.project.domain.users.Users;

public class OrderFixture {
    public static Orders createOrder(Users user) {
        Orders order = Orders.builder()
                .status(OrderStatus.PENDING)
                .user(user)
                .build();

        return order;
    }

    public static OrderItems createOrderItem(Orders order, Products product, int quantity) {
        return OrderItems.builder()
                .order(order)
                .product(product)
                .price(product.getPrice())
                .quantity(quantity)
                .build();
    }
}
