package com.project.config;

import com.project.filter.JwtAuthenticationFilter;
import com.project.filter.RateLimitFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RateLimitFilter rateLimitFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("public_route", r -> r
                        .path("/public/**", "/auth/**")
                        .uri("http://localhost:8080"))
                .route("protected_api_route", r -> r
                        .path("/api/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .filter(rateLimitFilter))
                        .uri("http://localhost:8080"))
                .build();
    }
}
