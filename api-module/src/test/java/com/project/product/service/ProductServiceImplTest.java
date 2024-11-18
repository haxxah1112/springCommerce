package com.project.product.service;

import com.project.BrandFixture;
import com.project.ProductFixture;
import com.project.UserFixture;
import com.project.common.dto.ApiResponse;
import com.project.common.dto.ProductSearchDto;
import com.project.common.enums.Condition;
import com.project.domain.brands.Brands;
import com.project.domain.products.Categories;
import com.project.domain.products.Products;
import com.project.domain.products.cursor.ProductCursorResponseDto;
import com.project.domain.products.repository.ProductQueryRepository;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.users.UserRole;
import com.project.domain.users.Users;
import com.project.product.dto.ProductResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductQueryRepository productQueryRepository;

    @Mock
    private ProductConverter productConverter;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;


    Brands brandA, brandB;

    @BeforeEach
    public void setup() {
        Users user = UserFixture.createUser("userName", "email", UserRole.SELLER);

        brandA = BrandFixture.createBrand("brandA", user);
        brandB = BrandFixture.createBrand("brandB", user);
    }

    @Test
    @DisplayName("TOP 카테고리 상품만 반환되며, 할인율 높은순으로 정렬된다.")
    public void getProductsByCategoryTest() {
        //Given
        ProductSearchDto searchDto = ProductSearchDto.builder()
                .size(10)
                .condition(Condition.DISCOUNT)
                .category(Categories.TOP)
                .cursor("10")
                .build();

        Products topProduct1 = ProductFixture.createProduct("Top1", brandA, 1000, Categories.TOP, 200);
        Products topProduct2 = ProductFixture.createProduct("Top2", brandB, 1200, Categories.TOP, 100);

        List<ProductCursorResponseDto> topProductList = List.of(
                ProductFixture.createProductCursorResponseDto(topProduct1, "customCursor1"),
                ProductFixture.createProductCursorResponseDto(topProduct2, "customCursor2")
        );

        Slice<ProductCursorResponseDto> productSlice = new SliceImpl<>(topProductList, Pageable.ofSize(10), false);

        when(productQueryRepository.findAllWithFilters(searchDto)).thenReturn(productSlice);

        //When
        Slice<ProductCursorResponseDto> result = productService.getProductsByCategory(searchDto);

        //Then
        assertThat(result.getContent()).hasSize(2)
                .extracting("category", "brandName", "productName", "price")
                .containsExactlyInAnyOrder(
                        tuple(Categories.TOP, "brandA", "Top1", 1000),
                        tuple(Categories.TOP, "brandB", "Top2", 1200)
                );

        assertThat(result.getContent())
                .isSortedAccordingTo(Comparator.comparing(ProductCursorResponseDto::getSalesCount).reversed());
    }

    @Test
    @DisplayName("상품 ID로 상품 상세 정보를 조회한다.")
    void getProductDetailTest() {
        //Given
        Long productId = 1L;
        Products product = ProductFixture.createProduct("Top1", brandA, 1000, Categories.TOP, 200);

        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                .productId(product.getId())
                .brandName(product.getBrand().getName())
                .productName(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .discountRate(product.getDiscountRate())
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productConverter.convertToDto(product)).thenReturn(productResponseDto);

        //When
        ApiResponse<ProductResponseDto> result = productService.getProductDetail(productId);

        //Then
        assertThat(result.getStatus()).isEqualTo(ApiResponse.ResponseStatus.SUCCESS);
        assertThat(result.getData().getBrandName()).isEqualTo("brandA");
        assertThat(result.getData().getProductName()).isEqualTo("Top1");
        assertThat(result.getData().getCategory()).isEqualTo(Categories.TOP);
        assertThat(result.getData().getPrice()).isEqualTo(1000);
        assertThat(result.getData().getStockQuantity()).isEqualTo(200);
        assertThat(result.getData().getDiscountRate()).isEqualTo(0.0);
    }

}