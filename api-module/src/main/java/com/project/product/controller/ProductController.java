package com.project.product.controller;

import com.project.domain.products.Categories;
import com.project.product.dto.ProductResponseDto;
import com.project.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("api/products")
    public ResponseEntity<Page<ProductResponseDto>> getProductByCategory(
            @RequestParam("category") Categories categories,
            Pageable pageable
    ) {
        Page<ProductResponseDto> productPages = productService.getProductsByCategory(categories, pageable);
        return ResponseEntity.ok(productPages);
    }
}
