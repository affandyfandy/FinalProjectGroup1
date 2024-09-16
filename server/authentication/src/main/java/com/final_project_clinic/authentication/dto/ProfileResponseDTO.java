package com.final_project_clinic.authentication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDTO {
    private String full_name;
    private String email;
    private String password;
    private String role;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;
}
