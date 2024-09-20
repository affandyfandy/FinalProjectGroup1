package com.final_project_clinic.appointment_services.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTimeDTO {

    private LocalTime startWorkingHour;
    private LocalTime endWorkingHour;
    private Integer maxPatient;
}
