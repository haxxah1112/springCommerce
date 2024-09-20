package com.project.security;


import com.project.domain.users.UserRole;

import java.util.Date;

public record JwtPayload(Long userId, Date issuedAt, UserRole userRole) {
}
