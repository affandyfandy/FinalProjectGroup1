package com.final_project_clinic.gateway_service.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {
        "gateway.jwt.secret=testSecretKey",
        "gateway.jwt.header=Authorization"
})
class JwtConfigTest {

    @Autowired
    private JwtConfig jwtConfig;

    @Test
    void testGetSecretKey() {
        assertEquals("testSecretKey", jwtConfig.getSecretKey());
    }

    @Test
    void testGetHeader() {
        assertEquals("Authorization", jwtConfig.getHeader());
    }
}
