package com.project.product.service;

import com.project.common.dto.ApiResponse;
import com.project.common.dto.ProductSearchDto;
import com.project.domain.products.Products;
import com.project.domain.products.cursor.ProductCursorResponseDto;
import com.project.domain.products.repository.ProductQueryRepository;
import com.project.domain.products.repository.ProductRepository;
import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import com.project.product.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductConverter productConverter;
    private final ProductQueryRepository productQueryRepository;
    private final ProductRepository productRepository;

    @Override
    public Slice<ProductCursorResponseDto> getProductsByCategory(ProductSearchDto searchDto) {
        return productQueryRepository.findAllWithFilters(searchDto);
    }

    @Override
    @Transactional
    public ApiResponse<ProductResponseDto> getProductDetail(Long productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));

        product.increaseViewCount();
        productRepository.save(product);

        return ApiResponse.success(productConverter.convertToDto(product));
    }

}
