package com.project.domain.order;

import com.project.domain.products.Products;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class OrderItems {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Products product;

    private int quantity;
    private int price;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;

    public int getTotalPrice() {
        return this.price;
    }

    public void complete() {
        this.status = OrderItemStatus.COMPLETED;
        this.product.increaseSalesCount(quantity);
    }
}
