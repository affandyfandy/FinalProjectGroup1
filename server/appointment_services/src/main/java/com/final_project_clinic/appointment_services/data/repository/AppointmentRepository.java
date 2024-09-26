package com.final_project_clinic.appointment_services.data.repository;

import com.final_project_clinic.appointment_services.data.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    long countByDoctorIdAndDateAndStartTime(UUID doctorId, LocalDate date, LocalTime startTime);
    long countByDoctorIdAndDate(UUID doctorId, LocalDate date);
    boolean existsByDoctorIdAndDateAndStartTimeAndPatientId(UUID doctorId, LocalDate date, LocalTime startTime, UUID patientId);
    Page<Appointment> findByPatientId(UUID patientId, Pageable pageable);

    Page<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);
    boolean existsByDoctorIdAndDateAndStartTimeAndPatientIdAndAppointmentIdNot(
            UUID doctorId, LocalDate date, LocalTime startTime, UUID patientId, UUID appointmentId);

    int countByPatientId(UUID patientId);


}