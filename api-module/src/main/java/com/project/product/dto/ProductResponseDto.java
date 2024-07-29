package com.project.product.dto;

import com.project.domain.products.Categories;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductResponseDto {
    private Long productId;

    private String brandName;

    private String productName;

    private Categories category;

    private int price;

    private int stockQuantity;

    private double discountRate;
    
    private int salesCount;
}
