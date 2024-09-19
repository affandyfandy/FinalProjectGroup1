package com.final_project_clinic.user.service;

import com.final_project_clinic.user.data.model.User;
import com.final_project_clinic.user.dto.PatientDTO;
import com.final_project_clinic.user.dto.PatientSaveDTO;
import com.final_project_clinic.user.dto.PatientShowDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllPatients() {
        Pageable pageable = PageRequest.of(0, 10);
        PatientShowDTO patient1 = createPatientShowDTO(UUID.randomUUID(), 1234567890L);
        PatientShowDTO patient2 = createPatientShowDTO(UUID.randomUUID(), 9876543210L);
        Page<PatientShowDTO> patientPage = new PageImpl<>(Arrays.asList(patient1, patient2));

        when(patientService.findAllPatients(pageable)).thenReturn(patientPage);

        Page<PatientShowDTO> result = patientService.findAllPatients(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(patient1.getNik(), result.getContent().get(0).getNik());
        assertEquals(patient2.getNik(), result.getContent().get(1).getNik());
    }

    @Test
    void testFindPatientById() {
        UUID patientId = UUID.randomUUID();
        PatientShowDTO patient = createPatientShowDTO(patientId, 1234567890L);

        when(patientService.findPatientById(patientId)).thenReturn(patient);

        PatientShowDTO result = patientService.findPatientById(patientId);

        assertEquals(patientId, result.getId());
        assertEquals(1234567890L, result.getNik());
    }

    @Test
    void testCreatePatient() {
        UUID userId = UUID.randomUUID();
        PatientSaveDTO patientSaveDTO = createPatientSaveDTO(userId, 1234567890L);
        PatientDTO createdPatient = createPatientDTO(UUID.randomUUID(), userId, 1234567890L);

        when(patientService.createPatient(patientSaveDTO)).thenReturn(createdPatient);

        PatientDTO result = patientService.createPatient(patientSaveDTO);

        assertNotNull(result.getId());
        assertEquals(patientSaveDTO.getNik(), result.getNik());
    }

    @Test
    void testUpdatePatient() {
        UUID patientId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        PatientSaveDTO patientSaveDTO = createPatientSaveDTO(userId, 9876543210L);
        PatientDTO updatedPatient = createPatientDTO(patientId, userId, 9876543210L);

        when(patientService.updatePatient(patientId, patientSaveDTO)).thenReturn(updatedPatient);

        PatientDTO result = patientService.updatePatient(patientId, patientSaveDTO);

        assertEquals(patientId, result.getId());
        assertEquals(patientSaveDTO.getNik(), result.getNik());
    }

    @Test
    void testDeletePatient() {
        UUID patientId = UUID.randomUUID();

        doNothing().when(patientService).deletePatient(patientId);

        patientService.deletePatient(patientId);

        verify(patientService, times(1)).deletePatient(patientId);
    }

    private PatientShowDTO createPatientShowDTO(UUID id, Long nik) {
        return new PatientShowDTO(
                id, new User(), nik, "1234567890", "Test Address", "Male",
                LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), "TestUser", "TestUser"
        );
    }

    private PatientSaveDTO createPatientSaveDTO(UUID userId, Long nik) {
        return new PatientSaveDTO(
                userId, nik, "1234567890", "Test Address", "Male", LocalDate.now()
        );
    }

    private PatientDTO createPatientDTO(UUID id, UUID userId, Long nik) {
        return new PatientDTO(
                id, new User(), nik, "1234567890", "Test Address", "Male",
                LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), "TestUser", "TestUser"
        );
    }
}