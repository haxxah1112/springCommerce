package com.project.shipping.preparation;

import com.project.OrderFixture;
import com.project.PaymentFixture;
import com.project.UserFixture;
import com.project.domain.order.OrderStatus;
import com.project.domain.order.Orders;
import com.project.domain.payment.PaymentStatus;
import com.project.domain.payment.Payments;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class OrderProcessorTest {
    @InjectMocks
    private OrderProcessor orderProcessor;

    @Test
    void testProcess() throws Exception {
        // Given
        Orders order = OrderFixture.createOrder(UserFixture.createDefaultUser(), OrderStatus.COMPLETED);
        Payments payment = PaymentFixture.createPayment(order, PaymentStatus.COMPLETED);

        // When
        Orders processedOrder = orderProcessor.process(payment);

        // Then
        assertEquals(OrderStatus.PREPARING_FOR_SHIPMENT, processedOrder.getStatus());
    }
}
