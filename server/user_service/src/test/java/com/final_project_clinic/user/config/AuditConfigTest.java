package com.final_project_clinic.user.config;

import com.final_project_clinic.user.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuditConfigTest {

    @Autowired
    @Qualifier("auditorProvider") // Specify which bean to inject
    private AuditorAware<String> auditorAware;

    @Mock
    private JwtUtils jwtUtil;

    @Autowired
    private DateTimeProvider dateTimeProvider;

    @Test
    void testDateTimeProviderBeanWithValidToken() {
        assertNotNull(dateTimeProvider);

        Optional<LocalDateTime> dateTimeOpt = dateTimeProvider.getNow().map(dt -> (LocalDateTime) dt);
        assertTrue(dateTimeOpt.isPresent());
        LocalDateTime dateTime = dateTimeOpt.get();
        assertTrue(dateTime instanceof LocalDateTime);

        LocalDateTime now = LocalDateTime.now();
        assertTrue(dateTime.isBefore(now.plusSeconds(1)));
        assertTrue(dateTime.isAfter(now.minusSeconds(1)));
    }

    @Test
    void testAuditorAwareBean() {
        assertNotNull(auditorAware);

        Optional<String> auditor = auditorAware.getCurrentAuditor();
        assertTrue(auditor.isPresent());

        // Optionally, assert the value of the current auditor
        assertEquals("System", auditor.get());
    }

    @Test
    void testAuditorAwareImpl() {
        AuditorAwareImpl auditorAwareImpl = new AuditorAwareImpl();
        Optional<String> auditor = auditorAwareImpl.getCurrentAuditor();
        assertNotNull(auditor);
        assertEquals("System", auditor.orElse(null));
    }
}
