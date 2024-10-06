package com.project.review.service;

import com.project.common.exception.NotFoundException;
import com.project.common.exception.error.CustomError;
import com.project.domain.order.Orders;
import com.project.domain.products.Products;
import com.project.domain.review.Reviews;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.review.dto.ReviewRequestDto;
import com.project.security.UserContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewConverter {
    private final UserContextProvider userContextProvider;
    private final UserRepository userRepository;
    public Reviews convertRequestToReviewEntity(ReviewRequestDto request, Users user, Orders order, Products product) {
        return Reviews.builder()
                .user(user)
                .order(order)
                .product(product)
                .comment(request.getComment())
                .rating(request.getRating())
                .build();

    }

}
