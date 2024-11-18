package com.project.order.controller;

import com.project.ApiApplication;
import com.project.BrandFixture;
import com.project.ProductFixture;
import com.project.UserFixture;
import com.project.common.dto.ApiResponse;
import com.project.common.kafka.producer.StockProducer;
import com.project.domain.brands.Brands;
import com.project.domain.order.OrderItems;
import com.project.domain.order.Orders;
import com.project.domain.order.repository.OrderItemRepository;
import com.project.domain.order.repository.OrderRepository;
import com.project.domain.products.Categories;
import com.project.domain.products.Products;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.exception.NotFoundException;
import com.project.order.dto.OrderItemDto;
import com.project.order.dto.OrderRequestDto;
import com.project.order.dto.OrderResponseDto;
import com.project.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = ApiApplication.class)
@Transactional
class OrderControllerTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private StockProducer stockProducer;

    @Test
    @DisplayName("주문이 정상적으로 동작하고 재고 감소 카프카 이벤트를 발행한다.")
    void testSuccessfulOrderCreation() {
        // Given
        Users testUser = userRepository.save(UserFixture.createDefaultUser());
        Products testProduct = productRepository.save(ProductFixture.createDefaultProduct(1L));

        OrderRequestDto request = OrderRequestDto.builder()
                .userId(testUser.getId())
                .items(List.of(
                        OrderItemDto.builder()
                                .productId(testProduct.getId())
                                .quantity(5)
                                .build()
                ))
                .build();

        // When
        ApiResponse<OrderResponseDto> response = orderService.createOrder(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(ApiResponse.ResponseStatus.SUCCESS);

        Optional<Orders> createdOrder = orderRepository.findById(response.getData().getOrderId());
        assertThat(createdOrder.get().getOrderItems()).hasSize(1);
        assertThat(createdOrder.get().getTotalPrice()).isEqualTo(testProduct.getPrice() * 5);

        verify(stockProducer, times(1)).sendProductDecrementEvents(anyList());
    }

}