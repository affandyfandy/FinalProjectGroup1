//package com.final_project_clinic.gateway_service.config;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsConfigurationSource;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//class CorsConfigTest {
//
//    private final CorsConfig corsConfig = new CorsConfig();
//
//    @Test
//    void corsConfigurationSource_ShouldReturnValidCorsConfiguration() {
//        // Act
//        CorsConfigurationSource corsConfigurationSource = corsConfig.corsConfigurationSource();
//
//        // Assert
//        assertTrue(corsConfigurationSource instanceof UrlBasedCorsConfigurationSource);
//
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = (UrlBasedCorsConfigurationSource) corsConfigurationSource;
//
//        // Retrieve the CORS configuration for any path
//        CorsConfiguration configuration = urlBasedCorsConfigurationSource.getCorsConfiguration("/some-path");
//
//        // Assert
//        assertEquals(List.of("http://localhost:3000"), configuration.getAllowedOrigins());
//        assertEquals(List.of("GET", "POST", "OPTIONS", "PUT", "DELETE"), configuration.getAllowedMethods());
//        assertEquals(List.of("Authorization", "Content-Type", "api-key"), configuration.getAllowedHeaders());
//        assertTrue(configuration.getAllowCredentials());
//        assertEquals(List.of("Location"), configuration.getExposedHeaders());
//        assertEquals(3600L, configuration.getMaxAge());
//    }
//}
