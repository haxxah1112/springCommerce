package com.project.order.service;

import com.project.common.ApiResponse;
import com.project.order.dto.OrderRequestDto;

public interface OrderService {

    ApiResponse createOrder(OrderRequestDto request);

    void deleteOrder(Long orderId);
}
