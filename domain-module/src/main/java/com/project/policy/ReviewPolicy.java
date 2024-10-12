package com.project.policy;

import com.project.domain.order.OrderStatus;
import com.project.domain.order.Orders;
import com.project.exception.InvalidOrderStatusException;
import com.project.exception.error.CustomError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewPolicy {
    public void validateReviewCreation(Orders order) {
        if (!OrderStatus.CONFIRM.equals(order.getStatus())) {
            throw new InvalidOrderStatusException(CustomError.INVALID_ORDER_STATUS);
        }
    }
}
