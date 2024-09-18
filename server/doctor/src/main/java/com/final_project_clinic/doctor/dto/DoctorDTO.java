package com.final_project_clinic.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {

    private UUID id;
    private String name;
    private String specialization;
    private String identificationNumber;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private Integer patientTotal;
}
