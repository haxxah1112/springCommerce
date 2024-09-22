package com.project.domain.order;

import com.project.domain.BaseEntity;
import com.project.domain.users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
