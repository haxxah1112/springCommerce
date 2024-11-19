package com.project.product.controller;

import com.project.common.dto.ApiResponse;
import com.project.common.dto.ProductSearchDto;
import com.project.domain.products.cursor.ProductCursorResponseDto;
import com.project.product.dto.ProductResponseDto;
import com.project.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Slice<ProductCursorResponseDto>> getProductCursorByCategory(@ModelAttribute ProductSearchDto productSearchDto) {
        Slice<ProductCursorResponseDto> productPages = productService.getProductsByCategory(productSearchDto);
        return ResponseEntity.ok(productPages);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductDetail(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getProductDetail(productId));
    }
}
