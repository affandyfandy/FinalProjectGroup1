package com.final_project_clinic.appointment_services.controller;

import com.final_project_clinic.appointment_services.dto.AppointmentCreateDTO;
import com.final_project_clinic.appointment_services.dto.AppointmentDTO;
import com.final_project_clinic.appointment_services.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentCreateDTO appointmentCreateDTO) {
        AppointmentDTO appointment = appointmentService.createAppointment(appointmentCreateDTO);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    // New: Get all appointments
    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<AppointmentDTO> appointments = appointmentService.getAllAppointments(page, size);
        return ResponseEntity.ok(appointments);
    }

    // New: Get appointment by id
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable UUID id) {
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable UUID id,
            @RequestBody AppointmentCreateDTO appointmentCreateDTO) {
        AppointmentDTO updatedAppointment = appointmentService.updateAppointment(id, appointmentCreateDTO);
        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDTO> cancelAppointment(@PathVariable UUID id) {
        AppointmentDTO updatedAppointment = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(updatedAppointment);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatientId(
            @PathVariable UUID patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByPatientId(patientId, page, size);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctorId(
            @PathVariable UUID doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByDoctorId(doctorId, page, size);
        return ResponseEntity.ok(appointments);
    }

}
