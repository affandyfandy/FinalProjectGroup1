package com.final_project_clinic.user.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.final_project_clinic.user.dto.PatientDTO;
import com.final_project_clinic.user.dto.PatientSaveDTO;
import com.final_project_clinic.user.dto.PatientShowDTO;
import com.final_project_clinic.user.service.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Get all patients with pagination
    @GetMapping
    public ResponseEntity<Page<PatientShowDTO>> getAllPatients(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PatientShowDTO> patients = patientService.findAllPatients(pageable);

        if (patients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    // Get patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<PatientShowDTO> getPatientById(@PathVariable UUID id) {
        PatientShowDTO patient = patientService.findPatientById(id);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    // Create new patient
    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientSaveDTO patientSaveDTO) {
        PatientDTO createdPatient = patientService.createPatient(patientSaveDTO);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    // Update existing patient
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(
            @PathVariable UUID id,
            @Valid @RequestBody PatientSaveDTO patientSaveDTO) {
        PatientDTO updatedPatient = patientService.updatePatient(id, patientSaveDTO);
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }

    // Delete patient by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
