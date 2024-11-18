package com.project;

import com.project.domain.brands.Brands;
import com.project.domain.products.Categories;
import com.project.domain.products.Products;
import com.project.domain.products.cursor.ProductCursorResponseDto;

public class ProductFixture {
    public static Products createProduct(String name, Brands brand, int price, Categories category, int stockQuantity) {
        return Products.builder()
                .brand(brand)
                .name(name)
                .category(category)
                .price(price)
                .stockQuantity(stockQuantity)
                .discountRate(0)
                .build();
    }

    public static ProductCursorResponseDto createProductCursorResponseDto(Products product, String customCursor) {
        return ProductCursorResponseDto.builder()
                .productId(product.getId())
                .brandName(product.getBrand().getName())
                .productName(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .discountRate(product.getDiscountRate())
                .salesCount(product.getSalesCount())
                .customCursor(customCursor)
                .build();
    }
}
