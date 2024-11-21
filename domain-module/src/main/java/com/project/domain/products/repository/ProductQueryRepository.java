package com.project.domain.products.repository;

import com.project.common.dto.ProductSearchDto;
import com.project.domain.products.Products;
import com.project.domain.products.cursor.ProductCursorResponseDto;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ProductQueryRepository {
    Slice<ProductCursorResponseDto> findAllWithFilters(ProductSearchDto searchDto);

}
