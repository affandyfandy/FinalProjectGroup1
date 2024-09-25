package com.final_project_clinic.doctor.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import com.final_project_clinic.doctor.data.model.Doctor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleShowDTO {
    private UUID id;
    private Doctor doctor;
    private String day;
    private List<ScheduleTimeDTO> scheduleTimes;
}
