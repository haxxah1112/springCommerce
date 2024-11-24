package com.project.domain.order;

import com.project.domain.BaseEntity;
import com.project.domain.users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private int totalPrice;

    private LocalDateTime deliveryCompletedAt;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private List<OrderItems> orderItems = new ArrayList<>();

    public void prepareForShipment() {
        this.status = OrderStatus.PREPARING_FOR_SHIPMENT;
    }

    public void confirmPurchase() {
        this.status = OrderStatus.CONFIRM;
    }

    public void complete() {
        this.totalPrice = calculateTotalPrice();
        this.status = OrderStatus.COMPLETED;
        this.orderItems.forEach(OrderItems::complete);
    }

    public void addOrderItems(List<OrderItems> items) {
        this.orderItems.addAll(items);
    }

    private int calculateTotalPrice() {
        return this.orderItems.stream()
                .mapToInt(OrderItems::getTotalPrice)
                .sum();
    }
}
