package com.project.shipping.preparation;

import com.project.OrderFixture;
import com.project.PaymentFixture;
import com.project.UserFixture;
import com.project.domain.order.OrderStatus;
import com.project.domain.order.Orders;
import com.project.domain.payment.PaymentStatus;
import com.project.domain.payment.Payments;
import com.project.domain.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompletedPaymentReaderTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private CompletedPaymentReader paymentReader;


    @Test
    void testRead() throws Exception {
        //Given
        Orders order = OrderFixture.createOrder(UserFixture.createDefaultUser(), OrderStatus.COMPLETED);
        Payments payment1 = PaymentFixture.createPayment(order, PaymentStatus.COMPLETED);
        Payments payment2 = PaymentFixture.createPayment(order, PaymentStatus.CANCELED);
        Payments payment3 = PaymentFixture.createPayment(order, PaymentStatus.COMPLETED);
        List<Payments> payments = Arrays.asList(payment1, payment3);

        when(paymentRepository.findByStatus(PaymentStatus.COMPLETED))
                .thenReturn(payments)
                .thenReturn(Collections.emptyList());

        //When
        Payments firstRead = paymentReader.read();
        Payments secondRead = paymentReader.read();
        Payments thirdRead = paymentReader.read();

        //Then
        assertEquals(payment1, firstRead);
        assertEquals(payment3, secondRead);
        assertEquals(null, thirdRead);
    }
}