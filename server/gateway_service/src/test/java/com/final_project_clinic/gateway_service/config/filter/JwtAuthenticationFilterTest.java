package com.final_project_clinic.gateway_service.config.filter;

import com.final_project_clinic.gateway_service.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private ServerHttpResponse response;

    @Mock
    private GatewayFilterChain chain;

    // Use the provided valid token
    private final String validToken = "eyJhbGciOiJSUzI1NiJ9.eyJyb2xlIjoiU1VQRVJBRE1JTiIsImlkIjoiMWM0NmExZGQtNDFlMS00Nzc1LWJkY2YtNmQ0YzZkODk4Nzc5IiwiZW1haWwiOiJlbWFpbDFkMjEzMjFAZ21haWwuY29tIiwic3ViIjoiZW1haWwxZDIxMzIxQGdtYWlsLmNvbSIsImlhdCI6MTcyNjcwNjAwOSwiZXhwIjoxNzI2NzQyMDA5fQ.TPcqK7vlBW0kJneym-opC9im-Cfr2JS_jDKAzRmHEkrET5_uIKVIELUT0k5CFDmaFcRazSIaLkY4f2wxzBAayRF1wtaaUa8edsOHfbAqlhWIBqKnvug2Y2Brg25YXIGelPgt6dZ22VGuqqnrQD1P2h7USIcALyLCSddHKHcNOchF7Ufy1qiv3Zxf0s8Qm6lELYZvZ39d5bCJlAKcXcotSGukKNo_AGfokiqtjQRXjfHkYG-v6-HkxpzhNzWa4q1PUApSIl89mZRLrTjbYq0f64TGPMlacWNZQyVQGmPn0hdAnO_QUejtL-24tukB8IFUFjF8Sqbe-kLVFfGj7Ahqvw";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFilter_ValidToken() {
        String token = "Bearer " + validToken;
        when(exchange.getRequest()).thenReturn(request);
        HttpHeaders headers = new HttpHeaders();
        headers.put("Authorization", List.of(token));
        when(request.getHeaders()).thenReturn(headers);

        // Mocking a valid URI
        URI uri = URI.create("/api/v1/authentication/profile");
        when(request.getURI()).thenReturn(uri);

        when(jwtUtils.validateToken(validToken)).thenReturn(true);
        when(jwtUtils.extractEmail(validToken)).thenReturn("email1d21321@gmail.com");
        when(jwtUtils.extractUserId(validToken)).thenReturn("1c46a1dd-41e1-4775-bdcf-6d4c6d898779");
        when(jwtUtils.extractUserRole(validToken)).thenReturn("SUPERADMIN");
        when(exchange.getResponse()).thenReturn(response);

        Mono<Void> result = jwtAuthenticationFilter.filter(exchange, chain);

        verify(chain).filter(any());
        verify(response, never()).setStatusCode(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testFilter_NoAuthorizationHeader() {
        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(new HttpHeaders()); // No headers
        when(exchange.getResponse()).thenReturn(response);

        when(request.getURI()).thenReturn(URI.create("/api/v1/some-other-endpoint"));

        Mono<Void> result = jwtAuthenticationFilter.filter(exchange, chain);

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(response).setComplete();
        verify(chain, never()).filter(any());
    }

    @Test
    void testFilter_InvalidTokenFormat() {
        String token = "InvalidTokenFormat"; // Not starting with "Bearer "
        when(exchange.getRequest()).thenReturn(request);
        HttpHeaders headers = new HttpHeaders();
        headers.put("Authorization", List.of(token));
        when(request.getHeaders()).thenReturn(headers);
        when(exchange.getResponse()).thenReturn(response);

        when(request.getURI()).thenReturn(URI.create("/api/v1/some-other-endpoint"));

        Mono<Void> result = jwtAuthenticationFilter.filter(exchange, chain);

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(response).setComplete();
        verify(chain, never()).filter(any());
    }

    @Test
    void testFilter_InvalidToken() {
        String token = "Bearer invalid.token.here"; // Valid format but invalid token
        when(exchange.getRequest()).thenReturn(request);
        HttpHeaders headers = new HttpHeaders();
        headers.put("Authorization", List.of(token));
        when(request.getHeaders()).thenReturn(headers);
        when(exchange.getResponse()).thenReturn(response);
        when(jwtUtils.validateToken("invalid.token.here")).thenReturn(false);

        when(request.getURI()).thenReturn(URI.create("/api/v1/some-other-endpoint"));

        Mono<Void> result = jwtAuthenticationFilter.filter(exchange, chain);

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(response).setComplete();
        verify(chain, never()).filter(any());
    }


    @Test
    void testGetOrder() {
        int order = jwtAuthenticationFilter.getOrder();
        assertEquals(-1, order);
    }
}
