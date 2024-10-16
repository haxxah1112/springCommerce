package com.project.product.service;

import com.project.common.dto.ApiResponse;
import com.project.common.dto.SearchDto;
import com.project.domain.products.Products;
import com.project.domain.products.repository.ProductQueryRepository;
import com.project.domain.products.repository.ProductRepository;
import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import com.project.product.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductConverter productConverter;
    private final ProductQueryRepository productQueryRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<ProductResponseDto> getProductsByCategory(SearchDto searchDto) {
        return productQueryRepository.findAllWithFilters(searchDto).map(productConverter::convertToDto);
    }

    @Override
    public ApiResponse<ProductResponseDto> getProductDetail(Long productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));

        product.increaseViewCount();
        productRepository.save(product);

        return ApiResponse.success(productConverter.convertToDto(product));
    }

}
