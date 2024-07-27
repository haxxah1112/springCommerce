package com.project.domain.products.repository;

import com.project.common.dto.SearchDto;
import com.project.domain.products.Products;
import org.springframework.data.domain.Page;

public interface ProductQueryRepository {
    Page<Products> findAllWithFilters(SearchDto criteria);
}
