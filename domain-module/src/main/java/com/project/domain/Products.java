package com.project.domain;

import com.project.domain.brands.Brands;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Products extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandId")
    private Brands brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Subcategories subcategory;

    private String name;
    private int price;
    private int stockQuantity;
    private int salesCount;
    private double discountRate;
}
