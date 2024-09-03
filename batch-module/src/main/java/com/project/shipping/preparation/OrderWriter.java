package com.project.shipping.preparation;

import com.project.domain.order.Orders;
import com.project.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderWriter implements ItemWriter<Orders> {
    private final OrderRepository orderRepository;


    @Override
    public void write(Chunk<? extends Orders> chunk) throws Exception {
        orderRepository.saveAll(chunk);
    }
}
