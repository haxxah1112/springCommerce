package com.project;

import com.project.domain.users.UserRole;
import com.project.domain.users.Users;

public class UserFixture {
    public static Users createUser(String userName, String email, UserRole userRole) {
        return Users.builder()
                .name(userName)
                .email(email)
                .userRole(userRole)
                .build();
    }

    public static Users createDefaultUser() {
        return Users.builder()
                .name("test")
                .email("test@test.com")
                .userRole(UserRole.BUYER)
                .build();
    }
}
