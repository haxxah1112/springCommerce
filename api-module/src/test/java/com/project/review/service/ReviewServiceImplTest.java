package com.project.review.service;

import com.project.BrandFixture;
import com.project.OrderFixture;
import com.project.ProductFixture;
import com.project.UserFixture;
import com.project.common.dto.ApiResponse;
import com.project.domain.brands.Brands;
import com.project.domain.order.OrderStatus;
import com.project.domain.order.Orders;
import com.project.domain.order.repository.OrderRepository;
import com.project.domain.products.Categories;
import com.project.domain.products.Products;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.review.Reviews;
import com.project.domain.review.repository.ReviewRepository;
import com.project.domain.users.UserRole;
import com.project.domain.users.Users;
import com.project.review.dto.ReviewRequestDto;
import com.project.security.UserContextProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
    @Mock
    private UserContextProvider userContextProvider;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReviewConverter reviewConverter;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Users user;
    private Orders order;
    private Products product;

    @BeforeEach
    public void setup() {
        user = UserFixture.createUser("userName", "email", UserRole.SELLER);
        Brands brand = BrandFixture.createBrand("brandName", user);
        product = ProductFixture.createProduct("top", brand, 200, Categories.TOP, 30);
        order = OrderFixture.createOrder(user, OrderStatus.CONFIRM);
    }

    @Test
    void createReviewTest() {
        //Given
        ReviewRequestDto requestDto = ReviewRequestDto.builder()
                .orderId(order.getId())
                .productId(product.getId())
                .comment("product review")
                .rating(5)
                .build();

        Reviews review = reviewConverter.convertRequestToReviewEntity(requestDto, user, order, product);
        when(userContextProvider.getCurrentUser()).thenReturn(user);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(reviewConverter.convertRequestToReviewEntity(requestDto, user, order, product)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);

        //When
        ApiResponse response = reviewService.createReview(requestDto);

        //Then
        assertEquals(ApiResponse.ResponseStatus.SUCCESS, response.getStatus());
    }
}