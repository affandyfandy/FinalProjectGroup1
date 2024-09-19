package com.final_project_clinic.user.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class AuditorAwareImplTest {

    private AuditorAwareImpl auditorAware;

    @BeforeEach
    void setUp() {
        auditorAware = new AuditorAwareImpl();
        SecurityContextHolder.clearContext(); // Clear context before each test
    }

    @Test
    void testGetCurrentAuditor_WhenAuthenticated() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("testUser", null, java.util.Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertEquals("testUser", auditorAware.getCurrentAuditor().orElse(null));
    }
}