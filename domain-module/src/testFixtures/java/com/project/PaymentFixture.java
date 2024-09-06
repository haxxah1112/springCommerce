package com.project;

import com.project.domain.order.Orders;
import com.project.domain.payment.PaymentStatus;
import com.project.domain.payment.Payments;

public class PaymentFixture {
    public static Payments createPayment(Orders order, PaymentStatus status) {
        return Payments.builder()
                .order(order)
                .status(status)
                .build();
    }
}
