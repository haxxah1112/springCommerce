package com.project.product.service;

import com.project.common.dto.ApiResponse;
import com.project.common.dto.SearchDto;
import com.project.product.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    Page<ProductResponseDto> getProductsByCategory(SearchDto searchDto);

    ApiResponse<ProductResponseDto> getProductDetail(Long productId);
}
