package com.final_project_clinic.user.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.final_project_clinic.user.data.model.Patient;
import com.final_project_clinic.user.data.repository.PatientRepository;
import com.final_project_clinic.user.dto.PatientDTO;
import com.final_project_clinic.user.dto.PatientSaveDTO;
import com.final_project_clinic.user.dto.PatientShowDTO;
import com.final_project_clinic.user.mapper.PatientMapper;
import com.final_project_clinic.user.service.PatientService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
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
                .orElseThrow(() -> new EntityNotFoundException("Patient with id " + id + " not found."));
        return patientMapper.toPatientShowDTO(patient);
    }

    @Override
    public PatientDTO createPatient(PatientSaveDTO patientSaveDTO) {
        Patient patient = patientMapper.toPatient(patientSaveDTO);
        patient.setCreatedTime(new Date());
        patient.setCreatedBy("admin"); // Replace with actual user from context
        patient = patientRepository.save(patient);
        return patientMapper.toPatientDTO(patient);
    }

    @Override
    public PatientDTO updatePatient(UUID id, PatientSaveDTO patientSaveDTO) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient with id " + id + " not found."));

        // Update fields from save DTO
        existingPatient.setAddress(patientSaveDTO.getAddress());
        existingPatient.setPhoneNumber(patientSaveDTO.getPhoneNumber());
        existingPatient.setDateOfBirth(patientSaveDTO.getDateOfBirth());
        existingPatient.setGender(patientSaveDTO.getGender());
        existingPatient.setNik(patientSaveDTO.getNik());
        existingPatient.setUpdatedTime(new Date());
        existingPatient.setUpdatedBy("adm in"); // Replace with actual user from context

        existingPatient = patientRepository.save(existingPatient);
        return patientMapper.toPatientDTO(existingPatient);
    }

    @Override
    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new EntityNotFoundException("Patient with id " + id + " not found.");
        }
        patientRepository.deleteById(id);
    }
}
