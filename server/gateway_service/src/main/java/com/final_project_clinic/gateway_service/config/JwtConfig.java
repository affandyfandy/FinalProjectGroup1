package com.final_project_clinic.gateway_service.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

    @Value("${gateway.jwt.secret}")
    private String secretKey;

    @Value("${gateway.jwt.header}")
    private String header;

    public String getSecretKey() {
        return secretKey;
    }

    public String getHeader() {
        return header;
    }
}