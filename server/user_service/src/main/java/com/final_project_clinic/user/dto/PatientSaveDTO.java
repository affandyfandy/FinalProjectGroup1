package com.final_project_clinic.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientSaveDTO {
    private UUID user_id;
    private Long nik;
    private String phoneNumber;
    private String address;
    private String gender;
    private LocalDate dateOfBirth;
}
