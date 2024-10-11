package com.project.review.service;

import com.project.*;
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
import com.project.policy.ReviewPolicy;
import com.project.review.dto.ReviewRequestDto;
import com.project.review.dto.ReviewResponseDto;
import com.project.security.UserContextProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private ReviewPolicy reviewPolicy;

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
        String comment = "product review";
        int rating = 5;

        ReviewRequestDto requestDto = ReviewRequestDto.builder()
                .orderId(order.getId())
                .productId(product.getId())
                .comment(comment)
                .rating(rating)
                .build();

        Reviews review = ReviewFixture.createReview(user, order, product, comment, rating);

        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .reviewId(review.getId())
                .orderId(order.getId())
                .productId(product.getId())
                .comment(comment)
                .rating(rating)
                .userId(user.getId())
                .build();

        when(userContextProvider.getCurrentUser()).thenReturn(user);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(reviewConverter.convertRequestToReviewEntity(requestDto, user, order, product)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);
        doNothing().when(reviewPolicy).validateReviewCreation(order);
        when(reviewConverter.convertEntityToResponseDto(review)).thenReturn(responseDto);

        //When
        ApiResponse apiResponse = reviewService.createReview(requestDto);

        //Then
        assertEquals(ApiResponse.ResponseStatus.SUCCESS, apiResponse.getStatus());

        ReviewResponseDto response = (ReviewResponseDto) apiResponse.getData();
        assertEquals(responseDto.getUserId(), response.getUserId());
        assertEquals(responseDto.getOrderId(), response.getOrderId());
        assertEquals(responseDto.getProductId(), response.getProductId());
        assertEquals(responseDto.getComment(), response.getComment());
        assertEquals(responseDto.getRating(), response.getRating());
    }

    @Test
    void createReview_fail() {
        //Given
        Orders pendingOrder = OrderFixture.createOrder(user, OrderStatus.PENDING);
        ReviewRequestDto failRequest = ReviewRequestDto.builder()
                .orderId(pendingOrder.getId())
                .productId(product.getId())
                .comment("product review")
                .rating(5)
                .build();

        when(userContextProvider.getCurrentUser()).thenReturn(user);
        when(orderRepository.findById(pendingOrder.getId())).thenReturn(Optional.of(pendingOrder));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        doThrow(new RuntimeException("Only confirmed orders can register reviews."))
                .when(reviewPolicy).validateReviewCreation(pendingOrder);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.createReview(failRequest);
        });

        assertEquals("Only confirmed orders can register reviews.", exception.getMessage());
    }

}