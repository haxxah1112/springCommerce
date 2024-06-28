package domain;

import jakarta.persistence.*;

@Entity
public class OrderItems {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Products product;

    private int quantity;
    private int price;
}
