package com.final_project_clinic.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleDTO {

    private UUID id;
    private UUID doctorId;
    private String day;
    private List<ScheduleTimeDTO> scheduleTimes;
}
