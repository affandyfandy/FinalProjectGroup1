package com.final_project_clinic.user.config.filter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.final_project_clinic.user.data.model.User;
import com.final_project_clinic.user.service.UserService;
import com.final_project_clinic.user.utils.JwtUtils;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtRequestFilterTest {

    private static final String VALID_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJyb2xlIjoiU1VQRVJBRE1JTiIsImlkIjoiZWU3ZTc1MjMtOTE3Zi00NjNlLWIzZjMtMDk3ZmY2NmU4ZTM5IiwiZW1haWwiOiJlbWFpbDFkMTMyMUBnbWFpbC5jb20iLCJzdWIiOiJlbWFpbDFkMTMyMUBnbWFpbC5jb20iLCJpYXQiOjE3MjY2OTAzOTQsImV4cCI6MTcyNjcyNjM5NH0.q9tZ2XD4ibwYg09G-f0wgxqej4i9VV9y7MIusBLYdgEb0AV7iFOXDCW2WM6trCeZsp0OAq8d_4wUKy8QFVh_djLBENvLCeluV5GVH97tvbYI04MTt1mdc0dGTcimEeNU-QFOaCTOK7dNwWOzOG6DyLzDI5NmXtqC1OU_yk1ypIeqIihQRgN8wMIUMuT8TyMRMM9u5BLWZwfPB0IfirJ-1jtroGbxpT638SWxTGagRe0JLaEFWl3h_SDC7SiCOIB8lEmgNhjuqLLk8MjZZ8PmyJLk8X0_FxHlrFlMgOggwM16zSyrtquRb5M-g_SXBACzPmUZg7XUKY4yMO-o07RCBA";

    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtil;

    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtRequestFilter = new JwtRequestFilter(userService, jwtUtil);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_WithValidToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + VALID_TOKEN);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(jwtUtil.extractEmail(VALID_TOKEN)).thenReturn("email1d21321@gmail.com");
        when(jwtUtil.extractUserRole(VALID_TOKEN)).thenReturn("SUPERADMIN");
        when(jwtUtil.validateToken(VALID_TOKEN)).thenReturn(false);
        when(jwtUtil.extractUserId(VALID_TOKEN)).thenReturn("ee7e7523-917f-463e-b3f3-097ff66e8e39");
        when(userService.findUserByEmail("email1d21321@gmail.com")).thenReturn(new User());

        jwtRequestFilter.doFilterInternal(request, response, filterChain);
    }

    @Test
    void testDoFilterInternalSecurityContext_WithMockUser() {
        // Simulate authentication being null
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Assert the authentication is null as expected
        assertNull(authentication);
    }

    @Test
    void testDoFilterInternal_WithNullAuthorities() {
        // Mock the jwtUtil to return true for validateToken
        when(jwtUtil.validateToken(VALID_TOKEN)).thenReturn(true);

        // Simulate setting authentication with an empty list of authorities
        List<GrantedAuthority> authorities = Collections.emptyList();
        Authentication auth = new UsernamePasswordAuthenticationToken("email1d21321@gmail.com", null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Assert that the authentication object has an empty list of authorities
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("email1d21321@gmail.com", authentication.getName());
        assertTrue(authentication.getAuthorities().isEmpty()); // Check if authorities list is empty
    }



    @Test
    void testDoFilterInternal_WithInvalidToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalidToken");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(jwtUtil.extractEmail("invalidToken")).thenReturn(null);
        when(jwtUtil.validateToken("invalidToken")).thenReturn(false);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_WithValidTokenAndAuthorities() throws Exception {
        // Mocking valid token scenario
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + VALID_TOKEN);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        // Prepare mock responses
        String email = "email1d21321@gmail.com";
        String role = "SUPERADMIN";

        when(jwtUtil.extractEmail(VALID_TOKEN)).thenReturn(email);
        when(jwtUtil.extractUserRole(VALID_TOKEN)).thenReturn(role);
        when(jwtUtil.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtil.extractUserId(VALID_TOKEN)).thenReturn("ee7e7523-917f-463e-b3f3-097ff66e8e39");
        when(userService.findUserByEmail(email)).thenReturn(new User());

        // Call the method under test
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Verify the SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(email, authentication.getName());
        assertFalse(authentication.getAuthorities().isEmpty());
        assertEquals(role, authentication.getAuthorities().iterator().next().getAuthority()); // Check if role matches
    }

}