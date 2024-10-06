package com.project.domain.review;

import com.project.domain.BaseEntity;
import com.project.domain.order.Orders;
import com.project.domain.products.Products;
import com.project.domain.users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Reviews extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Orders order;  // 주문과의 연관관계 추가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Products product;

    private int rating;

    @Column(columnDefinition = "TEXT")
    private String comment;
}
