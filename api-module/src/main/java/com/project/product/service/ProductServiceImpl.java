package com.project.product.service;

import com.project.common.dto.SearchDto;
import com.project.domain.products.repository.ProductQueryRepository;
import com.project.product.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductConverter productConverter;
    private final ProductQueryRepository productQueryRepository;

    @Override
    public Page<ProductResponseDto> getProductsByCategory(SearchDto searchDto) {
        return productQueryRepository.findAllWithFilters(searchDto).map(productConverter::convertToDto);
    }
}
