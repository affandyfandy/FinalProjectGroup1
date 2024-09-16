package com.final_project_clinic.user.data.model;

import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
@EntityListeners(AuditingEntityListener.class)
public class User extends Audit {

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
}
