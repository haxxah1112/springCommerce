package com.project.handler;

import com.project.domain.order.OrderStatus;
import com.project.domain.order.Orders;
import com.project.domain.order.repository.OrderRepository;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.event.AddPointEvent;
import com.project.policy.PointPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointHandler {

    private final PointPolicy pointPolicy;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @EventListener
    @Transactional
    public void handleAddPoint(AddPointEvent event) {
        Orders order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (OrderStatus.CONFIRM.equals(order.getStatus())) {
            int pointsToAdd = pointPolicy.calculatePoints(order.getTotalPrice());
            Users user = order.getUser();

            user.addPoint(pointsToAdd);
            userRepository.save(user);
        }
    }

}
