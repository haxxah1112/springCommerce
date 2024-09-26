package com.project.order.confirm;

import com.project.domain.order.Orders;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ConfirmPurchaseProcessor implements ItemProcessor<Orders, Orders> {

    @Override
    public Orders process(Orders order) {
        order.confirmPurchase();
        return order;
    }
}
