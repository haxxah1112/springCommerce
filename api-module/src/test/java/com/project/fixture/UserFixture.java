package com.project.fixture;

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
}
