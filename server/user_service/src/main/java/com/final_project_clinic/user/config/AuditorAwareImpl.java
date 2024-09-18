package com.final_project_clinic.user.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.final_project_clinic.user.data.model.User;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("System");
        }
        return Optional.of(authentication.getName());
        // if (authentication.getPrincipal() instanceof User user) {
        // return Optional.ofNullable(user.getEmail());
        // } else {
        // // Replace with appropriate extraction of email from the principal if
        // necessary
        // return Optional.of("Unknown");
        // }
    }
}
