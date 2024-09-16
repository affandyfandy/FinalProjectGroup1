package com.final_project_clinic.gateway_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsWebFilter;

@Configuration
public class GatewayConfig {

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    public CorsWebFilter corsWebFilter() {
        // Bridge the non-reactive CorsConfigurationSource to a reactive filter
        return new CorsWebFilter(corsConfig.corsConfigurationSource());
    }

}
