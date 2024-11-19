package com.project.order.service;

import com.project.common.dto.ApiResponse;
import com.project.order.dto.OrderRequestDto;

public interface OrderService {

    ApiResponse createOrder(OrderRequestDto request);

    ApiResponse confirmPurchase(Long orderId);
}
