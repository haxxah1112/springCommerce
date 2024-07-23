package com.project.product.service;

import com.project.domain.products.Categories;
import com.project.product.dto.ProductResponseDto;
import com.project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    @Override
    public Page<ProductResponseDto> getProductsByCategory(Categories categories, Pageable pageable) {
        return productRepository.findByCategory(categories, pageable).map(productConverter::convertToDto);
    }
}
