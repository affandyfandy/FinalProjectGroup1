package com.final_project_clinic.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaDoctorScheduleDTO {
    private String doctorName;
    private LocalDate date;
    private String specialization;
}
