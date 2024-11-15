package com.project.shipping.preparation;

import com.project.domain.order.OrderStatus;
import com.project.domain.order.Orders;
import com.project.domain.payment.Payments;
import com.project.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessor implements ItemProcessor<Payments, Orders> {

    @Override
    public Orders process(Payments payment) throws Exception {
        Orders order = payment.getOrder();
        if (order.getStatus() == OrderStatus.COMPLETED) {
            order.prepareForShipment();
        }
        return order;
    }
}
