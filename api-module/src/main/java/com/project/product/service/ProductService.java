package com.project.product.service;

import com.project.common.dto.ApiResponse;
import com.project.common.dto.ProductSearchDto;
import com.project.domain.products.cursor.ProductCursorResponseDto;
import com.project.product.dto.ProductResponseDto;
import org.springframework.data.domain.Slice;

public interface ProductService {
    Slice<ProductCursorResponseDto> getProductsByCategory(ProductSearchDto searchDto);

    ApiResponse<ProductResponseDto> getProductDetail(Long productId);
}
