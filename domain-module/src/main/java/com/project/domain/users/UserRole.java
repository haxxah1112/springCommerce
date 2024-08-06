package com.project.domain.users;

public enum UserRole {
    ALL,
    BUYER,
    SELLER;

    public static UserRole findUserRole(String code) {
        return UserRole.valueOf(code);
    }
}
