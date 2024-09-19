package com.final_project_clinic.user.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientSaveDTO {
    private UUID userId;

    @NotNull(message = "NIK cannot be null")
    private Long nik;

    @Pattern(regexp = "^\\d{10,20}$", message = "Phone number must be between 10 and 20 digits")
    private String phoneNumber;
    
    private String address;
    private String gender;
    private LocalDate dateOfBirth;
}
