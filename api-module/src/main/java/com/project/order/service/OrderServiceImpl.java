package com.project.order.service;

import com.project.common.dto.ApiResponse;
import com.project.common.dto.StockDecrementResult;
import com.project.domain.order.OrderItems;
import com.project.domain.order.Orders;
import com.project.domain.order.repository.OrderRepository;
import com.project.domain.products.Products;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.users.Users;
import com.project.event.AddPointEvent;
import com.project.event.stock.StockLogEvent;
import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import com.project.order.dto.OrderItemDto;
import com.project.order.dto.OrderRequestDto;
import com.project.order.dto.OrderResponseDto;
import com.project.product.service.ProductCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderConverter orderConverter;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ProductCacheService productCacheService;
    private final OrderValidator orderValidator;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public ApiResponse createOrder(OrderRequestDto request) {
        Users user = orderValidator.validateUser(request.getUserId());
        orderValidator.validateProducts(request.getItems());

        List<StockDecrementResult> stockDecrementResults = productCacheService.decrementStocks(request.getItems());

        try {
            Orders order = orderConverter.convertRequestDtoToOrderEntity(user);

            List<OrderItems> orderItems = createOrderItems(request.getItems());
            order.addOrderItems(orderItems);
            order.complete();
            orderRepository.save(order);

            StockLogEvent stockLogEvent = new StockLogEvent(stockDecrementResults, order.getId());
            applicationEventPublisher.publishEvent(stockLogEvent);

            OrderResponseDto responseDto = orderConverter.convertOrderEntityToOrderResponseDto(order);
            return ApiResponse.success(responseDto);
        } catch (Exception e) {
            productCacheService.rollbackStocks(stockDecrementResults);
            throw e;
        }

    }

    private List<OrderItems> createOrderItems(List<OrderItemDto> items) {
        return items.stream()
                .map(itemDto -> {
                    Products product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));
                    return orderConverter.convertOrderItemDtoToOrderItemEntity(itemDto, product);
                })
                .toList();
    }

    @Override
    @Transactional
    public ApiResponse confirmPurchase(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));
        order.confirmPurchase();
        Orders save = orderRepository.save(order);

        eventPublisher.publishEvent(new AddPointEvent(orderId));
        return ApiResponse.success(save);
    }
}
