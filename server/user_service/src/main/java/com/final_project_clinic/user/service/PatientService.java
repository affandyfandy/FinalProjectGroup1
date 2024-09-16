package com.final_project_clinic.user.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.final_project_clinic.user.dto.PatientDTO;
import com.final_project_clinic.user.dto.PatientSaveDTO;
import com.final_project_clinic.user.dto.PatientShowDTO;

public interface PatientService {

    // Find patients based on the provided criteria.
    Page<PatientShowDTO> findAllPatients(Pageable pageable);

    // Find patient by its id
    PatientShowDTO findPatientById(UUID id);

    // Creating a new patient.
    PatientDTO createPatient(PatientSaveDTO patientSaveDTO);

    // Updates an existing patient with the provided patient details.
    PatientDTO updatePatient(UUID id, PatientSaveDTO patientSaveDTO);

    // Delete a patient data by id.
    void deletePatient(UUID id);

}
