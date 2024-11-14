package com.project.order.service;

import com.project.common.dto.ApiResponse;
import com.project.common.kafka.message.DecrementProductStockMessage;
import com.project.common.kafka.producer.StockProducer;
import com.project.domain.order.OrderItems;
import com.project.domain.order.Orders;
import com.project.domain.order.repository.OrderItemRepository;
import com.project.domain.order.repository.OrderRepository;
import com.project.domain.products.Products;
import com.project.domain.products.StockLogs;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.users.Users;
import com.project.event.AddPointEvent;
import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import com.project.handler.StockLogHandler;
import com.project.order.dto.OrderItemDto;
import com.project.order.dto.OrderRequestDto;
import com.project.product.dto.StockDecrementResult;
import com.project.product.service.ProductCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderConverter orderConverter;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final StockProducer stockProducer;
    private final ApplicationEventPublisher eventPublisher;
    private final ProductCacheService productCacheService;
    private final OrderValidator orderValidator;
    private final StockLogHandler stockLogHandler;

    @Override
    @Transactional
    public ApiResponse createOrder(OrderRequestDto request) {
        Users user = orderValidator.validateUser(request.getUserId());
        orderValidator.validateProducts(request.getItems());

        List<StockDecrementResult> stockDecrementResults = productCacheService.decrementStocks(request.getItems());

        try {
            Orders order = orderConverter.convertRequestDtoToOrderEntity(request, user);
            List<OrderItems> orderItems = new ArrayList<>();
            for (OrderItemDto itemDto : request.getItems()) {
                Products product = productRepository.findById(itemDto.getProductId())
                        .orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));

                OrderItems orderItem = orderConverter.convertOrderItemDtoToOrderItemEntity(itemDto, order, product);
                orderItems.add(orderItem);
                orderItemRepository.save(orderItem);
            }

            order.complete(orderItems);
            orderRepository.save(order);

            List<StockLogs> stockLogs = stockLogHandler.saveStockLogs(stockDecrementResults, order);
            List<DecrementProductStockMessage> events = stockLogs.stream()
                    .map(log -> new DecrementProductStockMessage(
                            log.getProductId(),
                            log.getQuantity(),
                            log.getId()
                    ))
                    .toList();
            stockProducer.sendProductDecrementEvents(events);

            return ApiResponse.success(order);
        } catch (Exception e) {
            productCacheService.rollbackStocks(stockDecrementResults);
            throw e;
        }

    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));

        List<OrderItems> orderItems = orderItemRepository.findByOrderId(orderId);

        if (!orderItems.isEmpty()) {
            orderItemRepository.deleteAll(orderItems);
        }

        orderRepository.delete(order);
    }

    @Override
    public ApiResponse confirmPurchase(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));
        order.confirmPurchase();
        Orders save = orderRepository.save(order);

        eventPublisher.publishEvent(new AddPointEvent(orderId));
        return ApiResponse.success(save);
    }
}
