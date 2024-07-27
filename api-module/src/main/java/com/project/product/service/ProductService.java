package com.project.product.service;

import com.project.common.dto.SearchDto;
import com.project.product.dto.ProductResponseDto;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<ProductResponseDto> getProductsByCategory(SearchDto searchDto);
}
