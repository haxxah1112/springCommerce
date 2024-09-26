package com.project.domain.order.repository;

import com.project.domain.order.OrderStatus;
import com.project.domain.order.Orders;
import com.project.domain.order.QOrders;
import com.project.domain.products.QProducts;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QOrders order = QOrders.orders;

    @Override
    public List<Orders> findDeliveredOrders(LocalDateTime sevenDaysAgo) {
        return jpaQueryFactory
                .selectFrom(order)
                .where(
                        order.status.eq(OrderStatus.DELIVERED)
                                .and(order.deliveryCompletedAt.loe(sevenDaysAgo))
                )
                .fetch();
    }
}
