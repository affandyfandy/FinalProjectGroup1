package com.final_project_clinic.appointment_services.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FeignClientConfigTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private FeignClientConfig feignClientConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRequestInterceptorWithAuthorizationHeader() {
        // Setup mock request with Authorization header
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer mock-token");

        // Set the request to RequestContextHolder
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(httpServletRequest);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        // Call the interceptor and apply to a mock RequestTemplate
        RequestInterceptor interceptor = feignClientConfig.requestInterceptor();
        RequestTemplate requestTemplate = new RequestTemplate();
        interceptor.apply(requestTemplate);

        // Assert the Authorization header is set correctly
        assertTrue(requestTemplate.headers().containsKey("Authorization"));
        assertEquals("Bearer mock-token", requestTemplate.headers().get("Authorization").iterator().next());
    }

    @Test
    void testRequestInterceptorWithoutAuthorizationHeader() {
        // Setup mock request without Authorization header
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        // Set the request to RequestContextHolder
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(httpServletRequest);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        // Call the interceptor and apply to a mock RequestTemplate
        RequestInterceptor interceptor = feignClientConfig.requestInterceptor();
        RequestTemplate requestTemplate = new RequestTemplate();
        interceptor.apply(requestTemplate);

        // Assert the Authorization header is not set
        assertFalse(requestTemplate.headers().containsKey("Authorization"));
    }
}
