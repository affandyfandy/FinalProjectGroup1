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

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtUtils jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    public JwtRequestFilter(UserService userService, JwtUtils jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        logger.info("[JWTFilter][{}] [{}] {}", request, request.getMethod(), requestURI);

        // Extract JWT from the Authorization header
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = userService.findUserByEmail(email);
                System.out.println("data  users " + userDetails);
                System.out.println("User Role "+ userDetails.getRole());
                // logger.info("User Role: {}", userDetails.getRole());
                // Directly validate the JWT without authorities
                if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
                    // Only set authentication with null authorities
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userDetails.getRole()));

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        // Continue with the filter chain regardless of JWT token validation
        filterChain.doFilter(request, response);
    }
}
