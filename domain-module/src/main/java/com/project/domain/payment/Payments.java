package com.project.domain.payment;

import com.project.domain.BaseEntity;
import com.project.domain.order.Orders;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Payments extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Orders order;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
