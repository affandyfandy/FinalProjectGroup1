package com.final_project_clinic.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.final_project_clinic.user.data.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private UUID id;
    private User user;
    private Long nik;
    private String phoneNumber;
    private String address;
    private String gender;
    private LocalDate dateOfBirth;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String createdBy;
    private String updatedBy;
}

