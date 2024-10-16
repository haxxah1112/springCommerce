package com.project.order.controller;

import com.project.common.dto.ApiResponse;
import com.project.domain.users.UserRole;
import com.project.order.dto.OrderRequestDto;
import com.project.order.service.OrderService;
import com.project.security.ValidateToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping("api/v1/orders")
    @ValidateToken(checkLevel = UserRole.BUYER)
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequestDto request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PostMapping("api/v1/orders/{orderId}/confirm")
    public ResponseEntity<ApiResponse> confirmPurchase(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.confirmPurchase(orderId));
    }
}
