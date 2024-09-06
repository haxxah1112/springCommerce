package com.project;

import com.project.domain.brands.Brands;
import com.project.domain.products.Categories;
import com.project.domain.products.Products;

public class ProductFixture {
    public static Products createProduct(String name, Brands brand, int price, Categories category, int stockQuantity) {
        return Products.builder()
                .brand(brand)
                .name(name)
                .category(category)
                .price(price)
                .stockQuantity(stockQuantity)
                .discountRate(0)
                .build();
    }
}
