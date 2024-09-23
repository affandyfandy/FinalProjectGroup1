package com.final_project_clinic.appointment_services.dto;

import lombok.*;

import java.util.UUID;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private UUID userId;
    private String name;
    private String specialization;
    private String identificationNumber;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private Integer patientLimit;
}
