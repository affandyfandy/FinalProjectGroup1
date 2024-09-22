package com.final_project_clinic.appointment_services.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentUpdateDTO {

    @NotBlank
    private String status;

    private String initialComplaint;

    private LocalTime startTime;

    private LocalTime endTime;
}
