package com.project;

import com.project.domain.order.Orders;
import com.project.domain.products.Products;
import com.project.domain.review.Reviews;
import com.project.domain.users.Users;

public class ReviewFixture {
    public static Reviews createReview(Users user, Orders order, Products product, String comment, int rating) {
        return Reviews.builder()
                .user(user)
                .order(order)
                .product(product)
                .comment(comment)
                .rating(rating)
                .build();
    }
}
