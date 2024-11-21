package com.project.domain.products.cursor;

import com.project.domain.products.Categories;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class ProductCursorResponseDto {
    private Long productId;

    private String brandName;

    private String productName;

    private Categories category;

    private int price;

    private int stockQuantity;

    private double discountRate;

    private int salesCount;

    private String customCursor;
}
