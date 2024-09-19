package com.final_project_clinic.user.mapper;

import com.final_project_clinic.user.data.model.Patient;
import com.final_project_clinic.user.dto.PatientDTO;
import com.final_project_clinic.user.dto.PatientSaveDTO;
import com.final_project_clinic.user.dto.PatientShowDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PatientMapperTest {

    private final PatientMapper patientMapper = Mappers.getMapper(PatientMapper.class);

    @Test
    void testToPatientDTO() {
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID());
        patient.setGender("Male");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));

        PatientDTO patientDTO = patientMapper.toPatientDTO(patient);

        assertEquals(patient.getId(), patientDTO.getId());
        assertEquals(patient.getGender(), patientDTO.getGender());
        assertEquals(patient.getDateOfBirth(), patientDTO.getDateOfBirth());
    }

    @Test
    void testToPatient() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(UUID.randomUUID());
        patientDTO.setGender("Female");
        patientDTO.setDateOfBirth(LocalDate.of(1992, 2, 2));

        Patient patient = patientMapper.toPatient(patientDTO);

        assertEquals(patientDTO.getId(), patient.getId());
        assertEquals(patientDTO.getGender(), patient.getGender());
        assertEquals(patientDTO.getDateOfBirth(), patient.getDateOfBirth());
    }

    @Test
    void testToPatientShowDTO() {
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID());
        patient.setGender("Male");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));

        PatientShowDTO patientShowDTO = patientMapper.toPatientShowDTO(patient);

        assertEquals(patient.getId(), patientShowDTO.getId());
        assertEquals(patient.getGender(), patientShowDTO.getGender());
        assertEquals(patient.getDateOfBirth(), patientShowDTO.getDateOfBirth());
    }

    @Test
    void testToPatientFromPatientSaveDTO() {
        PatientSaveDTO patientSaveDTO = new PatientSaveDTO();
        patientSaveDTO.setGender("Male");
        patientSaveDTO.setAddress("PPP Street");
        patientSaveDTO.setDateOfBirth(LocalDate.of(1985, 3, 3));

        Patient patient = patientMapper.toPatient(patientSaveDTO);

        assertEquals(patientSaveDTO.getGender(), patient.getGender());
        assertEquals(patientSaveDTO.getDateOfBirth(), patient.getDateOfBirth());
        assertNull(patient.getId()); // Ensure id is not set
    }
}
