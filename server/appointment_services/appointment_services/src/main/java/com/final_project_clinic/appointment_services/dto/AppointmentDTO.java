package com.final_project_clinic.appointment_services.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class AppointmentDTO {
    private UUID appointmentId;
    private UUID patientId;
    private UUID doctorId;
    private LocalDate date;
    private String status;
    private String initialComplaint;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer queueNumber;
}
