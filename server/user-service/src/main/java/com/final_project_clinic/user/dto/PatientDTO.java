package com.final_project_clinic.user.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

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
    private Integer nik;
    private String phoneNumber;
    private String address;
    private String gender;
    private LocalDate dateOfBirth;
    private Date createdTime;
    private Date updatedTime;
    private String createdBy;
    private String updatedBy;
}

