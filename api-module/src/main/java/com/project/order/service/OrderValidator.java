package com.project.order.service;

import com.project.domain.products.Products;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import com.project.order.dto.OrderItemDto;
import com.project.product.service.ProductCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderValidator {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductCacheService productCacheService;

    public Users validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));
    }

    public void validateProducts(List<OrderItemDto> items) {
        for (OrderItemDto item : items) {
            String cachedProduct = productCacheService.getCachedProduct(item.getProductId());

            if (!StringUtils.hasText(cachedProduct)) {
                Products product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));

                productCacheService.cacheProduct(product);
            }
        }
    }
}
