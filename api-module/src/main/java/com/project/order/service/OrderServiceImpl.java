package com.project.order.service;

import com.project.common.ApiResponse;
import com.project.domain.order.OrderItems;
import com.project.domain.order.Orders;
import com.project.domain.order.repository.OrderRepository;
import com.project.domain.products.Products;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.order.dto.OrderItemDto;
import com.project.order.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderConverter orderConverter;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public ApiResponse createOrder(OrderRequestDto request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Orders order = orderConverter.convertRequestDtoToOrderEntity(request, user);
        order = orderRepository.save(order);

        for (OrderItemDto itemDto : request.getItems()) {
            Products product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItems orderItem = orderConverter.convertOrderItemDtoToOrderItemEntity(itemDto, order, product);
        }
        return ApiResponse.success(orderRepository.save(order));
    }
}
