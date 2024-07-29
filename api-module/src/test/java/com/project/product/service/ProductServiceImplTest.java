package com.project.product.service;

import com.project.common.dto.SearchDto;
import com.project.common.enums.Condition;
import com.project.domain.products.Categories;
import com.project.domain.products.Products;
import com.project.domain.products.repository.ProductQueryRepository;
import com.project.domain.products.repository.ProductQueryRepositoryImpl;
import com.project.fixture.ProductFixture;
import com.project.product.dto.ProductResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductQueryRepository productQueryRepository;

    @Mock
    private ProductConverter productConverter;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("TOP 카테고리 상품만 반환되며, 판매순으로 정렬된다.")
    public void getProductsByCategoryTest() {
        // Given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        SearchDto searchDto = SearchDto.builder()
                .page(page)
                .size(size)
                .condition(Condition.BEST)
                .category(Categories.TOP)
                .build();

        Products topProduct1 = ProductFixture.createProduct("BrandA", "Top1", Categories.TOP, 1000);
        Products topProduct2 = ProductFixture.createProduct("BrandB", "Top2", Categories.TOP, 1200);
        List<Products> topProducts = Arrays.asList(topProduct1, topProduct2);
        Page<Products> topProductPage = new PageImpl<>(topProducts, pageable, topProducts.size());


        when(productQueryRepository.findAllWithFilters(searchDto)).thenReturn(topProductPage);
        when(productConverter.convertToDto(any(Products.class))).thenAnswer(invocation -> {
            Products product = invocation.getArgument(0);
            return ProductResponseDto.builder()
                    .productId(product.getId())
                    .brandName(product.getBrand().getName())
                    .productName(product.getName())
                    .category(product.getCategory())
                    .price(product.getPrice())
                    .stockQuantity(product.getStockQuantity())
                    .discountRate(product.getDiscountRate())
                    .build();
        });


        // When
        Page<ProductResponseDto> result = productService.getProductsByCategory(searchDto);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).allSatisfy(dto ->
                assertThat(dto.getCategory()).isEqualTo(Categories.TOP));
        assertThat(result.getContent()).extracting(ProductResponseDto::getBrandName)
                .containsExactlyInAnyOrder("BrandA", "BrandB");
        assertThat(result.getContent()).extracting(ProductResponseDto::getProductName)
                .containsExactlyInAnyOrder("Top1", "Top2");
        assertThat(result.getContent()).extracting(ProductResponseDto::getPrice)
                .containsExactlyInAnyOrder(1000, 1200);
        assertThat(result.getContent()).isSortedAccordingTo(
                Comparator.comparing(ProductResponseDto::getSalesCount).reversed());

    }
}