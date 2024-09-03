package com.project.domain.order;

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
public class Orders {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public void prepareForShipment() {
        this.status = OrderStatus.PREPARING_FOR_SHIPMENT;
    }
}
