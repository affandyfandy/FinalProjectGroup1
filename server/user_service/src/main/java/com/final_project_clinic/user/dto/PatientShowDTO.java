package com.final_project_clinic.user.dto;

import java.time.LocalDate;
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
public class PatientShowDTO {
    private UUID id;
    private User user;
    private Integer nik;
    private String phoneNumber;
    private String address;
    private String gender;
    private LocalDate dateOfBirth;
}