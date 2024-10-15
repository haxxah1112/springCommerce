package com.project.product.controller;

import com.project.common.dto.ApiResponse;
import com.project.common.dto.SearchDto;
import com.project.product.dto.ProductResponseDto;
import com.project.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getProductByCategory(@ModelAttribute SearchDto searchDto) {
        Page<ProductResponseDto> productPages = productService.getProductsByCategory(searchDto);
        return ResponseEntity.ok(productPages);
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductDetail(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductDetail(productId));
    }
}
