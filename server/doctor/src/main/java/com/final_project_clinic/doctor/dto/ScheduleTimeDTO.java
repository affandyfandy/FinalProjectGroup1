package com.final_project_clinic.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTimeDTO {

    private LocalTime startWorkingHour;
    private LocalTime endWorkingHour;
    private Integer maxPatient;
}
