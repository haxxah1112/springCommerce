package com.project.common.kafka.consumer;

import com.project.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {
    private final OrderService orderService;

    @KafkaListener(topics = "order-rollback", groupId = "rollback-group")
    public void rollbackOrder(Long orderId){
        orderService.deleteOrder(orderId);
    }

}
