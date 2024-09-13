package com.final_project_clinic.user.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientSaveDTO {
    private Integer nik;
    private String phoneNumber;
    private String address;
    private String gender;
    private LocalDate dateOfBirth;
    private String createdBy;
}
