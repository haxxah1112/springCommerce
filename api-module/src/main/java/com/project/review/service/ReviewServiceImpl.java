package com.project.review.service;

import com.project.common.dto.ApiResponse;
import com.project.domain.order.Orders;
import com.project.domain.order.repository.OrderRepository;
import com.project.domain.products.Products;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.review.Reviews;
import com.project.domain.review.repository.ReviewRepository;
import com.project.domain.users.Users;
import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import com.project.policy.ReviewPolicy;
import com.project.review.dto.ReviewRequestDto;
import com.project.review.dto.ReviewResponseDto;
import com.project.security.UserContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewPolicy reviewPolicy;
    private final UserContextProvider userContextProvider;
    private final ReviewConverter reviewConverter;


    @Override
    public ApiResponse createReview(ReviewRequestDto request) {
        Users user = userContextProvider.getCurrentUser();
        Orders order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));
        Products product = productRepository.findById(request.getProductId()).orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));

        reviewPolicy.validateReviewCreation(order);

        Reviews review = reviewConverter.convertRequestToReviewEntity(request, user, order, product);
        Reviews savedReview = reviewRepository.save(review);

        ReviewResponseDto responseDto = reviewConverter.convertEntityToResponseDto(savedReview);
        return ApiResponse.success(responseDto);
    }
}
