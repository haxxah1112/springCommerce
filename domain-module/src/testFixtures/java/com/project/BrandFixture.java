package com.project;

import com.project.domain.brands.BrandStatus;
import com.project.domain.brands.Brands;
import com.project.domain.users.Users;

public class BrandFixture {
    public static Brands createBrand(String brandName, Users user) {
        return Brands.builder()
                .user(user)
                .name(brandName)
                .status(BrandStatus.ACTIVE)
                .build();
    }
}
