package com.final_project_clinic.user.config.filter;

import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.final_project_clinic.user.data.model.User;
import com.final_project_clinic.user.service.UserService;
import com.final_project_clinic.user.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtUtils jwtUtil;
    private static final Logger jwtLogger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    public JwtRequestFilter(UserService userService, JwtUtils jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {


        String requestURI = request.getRequestURI();
        jwtLogger.info("[JWTFilter][{}] [{}] {}", request, request.getMethod(), requestURI);

        // Extract JWT from the Authorization header
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);
            String role = jwtUtil.extractUserRole(jwt);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = userService.findUserByEmail(email);
                jwtLogger.info("Data users: {}", userDetails);
                jwtLogger.info("User Role: {}", userDetails.getRole());

                // Directly validate the JWT without authorities
                if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt))) {
                    // Only set authentication with null authorities
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            email, null, authorities);

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        // Continue with the filter chain regardless of JWT token validation
        filterChain.doFilter(request, response);
    }
}
