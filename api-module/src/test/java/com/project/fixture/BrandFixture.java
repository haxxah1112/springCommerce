package com.project.fixture;

import com.project.domain.brands.BrandStatus;
import com.project.domain.brands.Brands;
import com.project.domain.users.UserRole;
import com.project.domain.users.Users;

public class BrandFixture {
    public static Brands createBrand(String brandName) {
        return Brands.builder()
                .user(UserFixture.createUser("name", "test@test.com", UserRole.BUYER))
                .name(brandName)
                .status(BrandStatus.ACTIVE)
                .build();
    }
}
