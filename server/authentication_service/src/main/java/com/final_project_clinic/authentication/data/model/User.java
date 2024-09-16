package com.final_project_clinic.authentication.data.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
public class User implements UserDetails {
    
    @Id
    @Column(name = "ID", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String full_name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @Column(name = "created_time", nullable = false)
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "created_by", nullable = false, length = 255)
    private String createdBy;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
    
    @Override
    public String getUsername() {
        return email; // Return the email as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Set this to false if account expiration logic is implemented
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Set this to false if account locking logic is implemented
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Set this to false if credentials expiration logic is implemented
    }

    @Override
    public boolean isEnabled() {
        return true; // Set this to false if account enabling/disabling logic is implemented
    }
}

