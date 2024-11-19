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

    public void completed(int totalPrice) {
        this.totalPrice = totalPrice;
        this.status = OrderStatus.COMPLETED;
    }

    public void complete() {
        int totalPrice = this.orderItems.stream()
                .mapToInt(orderItem -> orderItem.getProduct().getPrice() * orderItem.getQuantity())
                .sum();

        this.totalPrice = totalPrice;
        this.status = OrderStatus.COMPLETED;
    }

    public void addOrderItems(List<OrderItems> items) {
        this.orderItems.addAll(items);
    }
}
