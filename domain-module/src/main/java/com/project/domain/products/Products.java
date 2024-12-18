package com.project.domain.products;

import com.project.domain.BaseEntity;
import com.project.domain.brands.Brands;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "products")
public class Products extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandId")
    private Brands brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Categories category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Subcategories subcategory;

    private String name;
    private int price;
    private int stockQuantity;
    private int salesCount;
    private double discountRate;
    private int viewCount = 0;

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void decreaseStock(int quantity) {
        this.stockQuantity -= quantity;
    }

    public void increaseViewCount() {
        this.viewCount += 1;
    }

    public void increaseSalesCount(int quantity) {
        this.salesCount += quantity;
    }
}
