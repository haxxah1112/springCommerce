package com.project.fixture;

import com.project.domain.products.Categories;
import com.project.domain.products.Products;

public class ProductFixture {
    public static Products createProduct(String brandName, String productName, Categories category, int price) {
        return Products.builder()
                .id(1L)
                .brand(BrandFixture.createBrand(brandName))
                .name(productName)
                .category(category)
                .price(price)
                .stockQuantity(30)
                .discountRate(0)
                .build();
    }
}
