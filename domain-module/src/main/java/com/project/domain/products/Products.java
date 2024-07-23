package com.project.domain.products;

import com.project.domain.BaseEntity;
import com.project.domain.brands.Brands;
import jakarta.persistence.*;
import lombok.Getter;

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
}