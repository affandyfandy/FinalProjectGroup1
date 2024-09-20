package com.final_project_clinic.appointment_services.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "Appointment")
@NoArgsConstructor
@AllArgsConstructor
public class Appointment extends Audit {

    @Id
    @GeneratedValue
    @Column(name = "appointment_id", nullable = false)
    private UUID appointmentId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "initial_complaint")
    private String initialComplaint;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "queue_number", nullable = false)
    private Integer queueNumber;
}
