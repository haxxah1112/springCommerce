package com.project.domain.order.repository;

import com.project.domain.order.Orders;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderQueryRepository {
    List<Orders> findDeliveredOrders(LocalDateTime sevenDaysAgo);
}
