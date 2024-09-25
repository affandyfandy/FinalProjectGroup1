package com.final_project_clinic.user.service.impl;

import java.util.UUID;

import com.final_project_clinic.user.exception.DuplicatePhoneNumberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.final_project_clinic.user.data.model.Patient;
import com.final_project_clinic.user.data.model.User;
import com.final_project_clinic.user.data.repository.PatientRepository;
import com.final_project_clinic.user.data.repository.UserRepository;
import com.final_project_clinic.user.dto.PatientDTO;
import com.final_project_clinic.user.dto.PatientSaveDTO;
import com.final_project_clinic.user.dto.PatientShowDTO;
import com.final_project_clinic.user.exception.DuplicateNikException;
import com.final_project_clinic.user.exception.ResourceNotFoundException;
import com.final_project_clinic.user.mapper.PatientMapper;
import com.final_project_clinic.user.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;

    private static final String PATIENT_NOT_FOUND = "Patient Not Found";
    private static final String USER_NOT_FOUND = "User Not Found";

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper,
            UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Page<PatientShowDTO> findAllPatients(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<Patient> products = patientRepository.findAll(sortedPageable);
        return products.map(patientMapper::toPatientShowDTO);
    }

    @Override
    public PatientShowDTO findPatientById(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PATIENT_NOT_FOUND));
        return patientMapper.toPatientShowDTO(patient);
    }

    @Override
    public Patient findPatientByUserId(UUID id) {
        Patient patient = patientRepository.findPatientByUserId(id);
        return patient;
    }

    @Override
    public PatientDTO createPatient(PatientSaveDTO patientSaveDTO) {
        // Find and validate the existing user
        User existingUser = userRepository.findById(patientSaveDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        USER_NOT_FOUND));

        // Check if the user already has a patient
        Patient existingPatientForUser = patientRepository.findByUser(existingUser);
        if (existingPatientForUser != null) {
            throw new IllegalArgumentException(
                    USER_NOT_FOUND);
        }

        // Check if another patient with the same NIK exists
        Patient existingPatientWithNik = patientRepository.findPatientByNik(patientSaveDTO.getNik());
        if (existingPatientWithNik != null) {
            throw new DuplicateNikException("NIK already exists");
        }

        // Check if another patient with the same phone number exists
        Patient existingPatientPhoneNumber = patientRepository
                .findPatientByPhoneNumber(patientSaveDTO.getPhoneNumber());
        if (existingPatientPhoneNumber != null) {
            throw new DuplicatePhoneNumberException("Phone number already exists");
        }

        // Map DTO to entity and set user
        Patient patient = patientMapper.toPatient(patientSaveDTO);
        patient.setUser(existingUser);

        // Save the new patient
        patient = patientRepository.save(patient);

        // Return the patient DTO
        return patientMapper.toPatientDTO(patient);
    }

    @Override
    public PatientDTO updatePatient(UUID id, PatientSaveDTO patientSaveDTO) {

        // Find and validate the existing patient
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PATIENT_NOT_FOUND));

        // Find and validate the existing user
        User existingUser = userRepository.findById(patientSaveDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        USER_NOT_FOUND));

        // Check if the user already has a patient
        Patient existingPatientForUser = patientRepository.findByUser(existingUser);

        // Allow update if the user is already associated with the patient being updated
        if (existingPatientForUser != null && !existingPatientForUser.getId().equals(id)) {
            throw new IllegalArgumentException(
                    "User already has a patient.");
        }

        // Check if another patient with the same NIK exists
        Patient existingPatientWithNik = patientRepository.findPatientByNik(patientSaveDTO.getNik());
        if (existingPatientWithNik != null && !existingPatientWithNik.getId().equals(id)) {
            throw new DuplicateNikException("NIK already exists");
        }

        // Check if another patient with the same phone number exists
        Patient existingPatientPhoneNumber = patientRepository
                .findPatientByPhoneNumber(patientSaveDTO.getPhoneNumber());
        if (existingPatientPhoneNumber != null && !existingPatientPhoneNumber.getId().equals(id)) {
            throw new DuplicatePhoneNumberException("Phone number already exists");
        }

        // Update fields from the save DTO
        existingPatient.setAddress(patientSaveDTO.getAddress());
        existingPatient.setPhoneNumber(patientSaveDTO.getPhoneNumber());
        existingPatient.setDateOfBirth(patientSaveDTO.getDateOfBirth());
        existingPatient.setGender(patientSaveDTO.getGender());
        existingPatient.setNik(patientSaveDTO.getNik());

        // Set the user for the patient
        existingPatient.setUser(existingUser);

        // Save updated patient entity
        existingPatient = patientRepository.save(existingPatient);

        // Return the updated patient DTO
        return patientMapper.toPatientDTO(existingPatient);
    }

    @Override
    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException(PATIENT_NOT_FOUND);
        }
        patientRepository.deleteById(id);
    }
}
