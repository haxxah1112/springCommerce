package com.project.shipping.preparation;

import com.project.domain.payment.PaymentStatus;
import com.project.domain.payment.Payments;
import com.project.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CompletedPaymentReader implements ItemReader<Payments> {

    private final PaymentRepository paymentRepository;
    private Iterator<Payments> currentIterator = Collections.emptyIterator();

    @Override
    public Payments read() throws Exception {
        if (!currentIterator.hasNext()) {
            List<Payments> payments = paymentRepository.findByStatus(PaymentStatus.COMPLETED);
            currentIterator = payments.iterator();
        }
        return currentIterator.hasNext() ? currentIterator.next() : null;
    }
}

