package com.final_project_clinic.appointment_services.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreateDTO {

    private UUID patientId;
    private UUID doctorId;
    private LocalDate date;
    private String initialComplaint;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer queueNumber;
}