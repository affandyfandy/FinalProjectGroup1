package com.final_project_clinic.appointment_services.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID userId;
    private String fullName;
    private String email;
    private String role;
}
