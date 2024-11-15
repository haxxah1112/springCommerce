package com.project.order.confirm;

import com.project.domain.order.Orders;
import com.project.domain.order.repository.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConfirmPurchaseReader implements ItemReader<Orders> {

    private final OrderQueryRepository orderQueryRepository;

    @Override
    public Orders read() {
        List<Orders> ordersList = orderQueryRepository.findDeliveredOrders(LocalDateTime.now().minusDays(7));
        return new ListItemReader<>(ordersList).read();
    }
}
