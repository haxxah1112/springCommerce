package com.project.security;


import com.project.domain.users.UserRole;

import java.util.Date;

public record JwtPayload(String email, Date issuedAt, UserRole userRole) {
}
