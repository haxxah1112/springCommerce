package com.project.order.service;

import com.project.ProductFixture;
import com.project.domain.products.Products;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import com.project.order.dto.OrderItemDto;
import com.project.product.service.ProductCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductCacheService productCacheService;

    private OrderValidator orderValidator;

    @BeforeEach
    public void setUp() {
        orderValidator = new OrderValidator(userRepository, productRepository, productCacheService);
    }

    @Test
    @DisplayName("사용자가 정상적으로 검증된다")
    public void validateUser_Success() {
        //Given
        Long userId = 1L;
        Users user = new Users();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //When
        Users result = orderValidator.validateUser(userId);

        //Then
        assertEquals(user, result);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("사용자가 존재하지 않을 경우 예외가 발생한다 ")
    public void validateUser_ThrowsNotFoundException() {
        //Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //When
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            orderValidator.validateUser(userId);
        });

        //Then
        assertEquals(CustomError.NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("주문 상품이 정상적으로 검증된다")
    public void validateProducts_Success() {
        //Given
        Long productId = 1L;
        OrderItemDto orderItem = new OrderItemDto(productId, 10, 2000);
        List<OrderItemDto> items = List.of(orderItem);
        Products product = ProductFixture.createDefaultProduct(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        //When
        orderValidator.validateProducts(items);

        //Then
        verify(productRepository).findById(productId);
        verify(productCacheService).getCachedProduct(productId);
        verify(productCacheService).cacheProduct(product);
    }

    @Test
    @DisplayName("상품이 존재하지 않을 경우 예외가 발생한다")
    public void validateProducts_ThrowsNotFoundException() {
        //Given
        Long productId = 1L;
        OrderItemDto orderItem = new OrderItemDto(productId, 10, 2000);
        List<OrderItemDto> items = List.of(orderItem);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //When
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            orderValidator.validateProducts(items);
        });

        //Then
        assertEquals(CustomError.NOT_FOUND, exception.getErrorCode());
    }
}