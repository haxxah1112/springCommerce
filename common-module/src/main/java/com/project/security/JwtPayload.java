package com.project.security;

import java.util.Date;

public record JwtPayload(String userId, Date issuedAt) {
}
