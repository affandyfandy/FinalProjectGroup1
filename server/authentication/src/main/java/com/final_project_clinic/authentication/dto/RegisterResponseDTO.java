package com.final_project_clinic.authentication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponseDTO {
    private String message = "Registered Successfully";
    private Long nik;
    private String full_name;
    private String email;
    private String password;
    private String role;
}
