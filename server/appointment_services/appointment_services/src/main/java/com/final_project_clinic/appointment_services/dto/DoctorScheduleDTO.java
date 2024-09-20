package com.final_project_clinic.appointment_services.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleDTO {

    private UUID id;
    private UUID doctorId;
    private String day;
    private List<ScheduleTimeDTO> scheduleTimes;
}
