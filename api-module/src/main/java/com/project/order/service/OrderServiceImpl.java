package com.project.order.service;

import com.project.common.ApiResponse;
import com.project.common.kafka.message.StockMessage;
import com.project.common.kafka.producer.StockProducer;
import com.project.domain.order.OrderItems;
import com.project.domain.order.Orders;
import com.project.domain.order.repository.OrderItemRepository;
import com.project.domain.order.repository.OrderRepository;
import com.project.domain.products.Products;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.order.dto.OrderItemDto;
import com.project.order.dto.OrderRequestDto;
import com.project.order.manager.StockResultManager;
import com.project.order.manager.StockResultContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderConverter orderConverter;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final StockProducer stockProducer;
    private final StockResultManager orderContextManager;

    @Override
    @Transactional
    public ApiResponse createOrder(OrderRequestDto request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Orders order = orderConverter.convertRequestDtoToOrderEntity(request, user);
        order = orderRepository.save(order);

        final Long orderId = order.getId();
        StockResultContext context = new StockResultContext(request.getItems().size(), request.getItems());
        orderContextManager.addContext(orderId, context);

        for (OrderItemDto itemDto : request.getItems()) {
            Products product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            stockProducer.sendOrderEvent(new StockMessage(orderId, itemDto.getProductId(), itemDto.getQuantity()));

            OrderItems orderItem = orderConverter.convertOrderItemDtoToOrderItemEntity(itemDto, order, product);
            orderItemRepository.save(orderItem);
        }

        return ApiResponse.success(orderRepository.save(order));
    }

    public void deleteOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItems> orderItems = orderItemRepository.findByOrderId(orderId);

        if (!orderItems.isEmpty()) {
            orderItemRepository.deleteAll(orderItems);
        }

        orderRepository.delete(order);
    }
}
