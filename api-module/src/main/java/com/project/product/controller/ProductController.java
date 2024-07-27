package com.project.product.controller;

import com.project.common.dto.SearchDto;
import com.project.product.dto.ProductResponseDto;
import com.project.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/api/products")
    public ResponseEntity<Page<ProductResponseDto>> getProductByCategory(@ModelAttribute SearchDto searchDto) {
        Page<ProductResponseDto> productPages = productService.getProductsByCategory(searchDto);
        return ResponseEntity.ok(productPages);
    }
}
