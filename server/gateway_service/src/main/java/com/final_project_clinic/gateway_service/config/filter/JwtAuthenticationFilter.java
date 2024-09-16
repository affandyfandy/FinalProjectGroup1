package com.final_project_clinic.gateway_service.config.filter;

import com.final_project_clinic.gateway_service.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
            org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Skip filter for authentication routes
        if (path.startsWith("/api/v1/authentication")) {
            return chain.filter(exchange);
        }

        // Get Authorization header from the request
        if (!request.getHeaders().containsKey("Authorization")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String authorizationHeader = request.getHeaders().getOrEmpty("Authorization").get(0);

        // Check if the token is valid (must start with "Bearer")
        if (!authorizationHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authorizationHeader.substring(7);

        // Validate JWT Token
        if (!jwtUtils.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Extract claims and pass them to downstream services
        String email = jwtUtils.extractEmail(token);
        String userId = jwtUtils.extractUserId(token);
        String role = jwtUtils.extractUserRole(token);

        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                .header("X-Authenticated-User", email)
                .header("X-User-Id", userId)
                .header("X-User-Role", role)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    @Override
    public int getOrder() {
        return -1; // Ensure the filter runs early
    }
}
