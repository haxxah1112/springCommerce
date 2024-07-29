package com.project.product.service;

import com.project.domain.products.Products;
import com.project.product.dto.ProductResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public ProductResponseDto convertToDto(Products product) {
        return ProductResponseDto.builder()
                .productId(product.getId())
                .brandName(product.getBrand().getName())
                .productName(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .discountRate(product.getDiscountRate())
                .salesCount(product.getSalesCount())
                .build();
    }
}
