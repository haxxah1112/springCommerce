package com.project.product.service;

import com.project.domain.products.Categories;
import com.project.product.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<ProductResponseDto> getProductsByCategory(Categories categories, Pageable pageable);
}
