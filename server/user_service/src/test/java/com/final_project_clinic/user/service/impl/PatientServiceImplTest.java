package com.final_project_clinic.user.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.final_project_clinic.user.exception.DuplicatePhoneNumberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private User user;
    private PatientDTO patientDTO;
    private PatientSaveDTO patientSaveDTO;
    private PatientShowDTO patientShowDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID patientId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        patient = new Patient();
        patient.setId(patientId);
        patient.setNik(317412341234L);
        patient.setPhoneNumber("1234567890");
        patient.setUser(user);

        user = new User();
        user.setId(userId);

        patientDTO = new PatientDTO();
        patientSaveDTO = new PatientSaveDTO();
        patientSaveDTO.setUserId(userId);
        patientSaveDTO.setNik(317412341234L);
        patientSaveDTO.setPhoneNumber("089512341234");

        patientShowDTO = new PatientShowDTO();
        patientShowDTO.setNik(317412341234L);
    }

    @Test
    public void testFindAllPatients() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Patient> patients = new PageImpl<>(List.of(patient), pageable, 1);

        when(patientRepository.findAll(pageable)).thenReturn(patients);
        when(patientMapper.toPatientShowDTO(any(Patient.class))).thenReturn(patientShowDTO);

        Page<PatientShowDTO> result = patientService.findAllPatients(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(patientShowDTO.getNik(), result.getContent().get(0).getNik());
        verify(patientRepository).findAll(pageable);
    }

    @Test
    public void testFindPatientById() {
        when(patientRepository.findById(any(UUID.class))).thenReturn(Optional.of(patient));
        when(patientMapper.toPatientShowDTO(any(Patient.class))).thenReturn(patientShowDTO);

        PatientShowDTO result = patientService.findPatientById(patient.getId());

        assertNotNull(result);
        assertEquals(patientShowDTO.getNik(), result.getNik());
        verify(patientRepository).findById(patient.getId());
    }

    @Test
    public void testCreatePatient() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(patientRepository.findPatientByNik(anyLong())).thenReturn(null);
        when(patientRepository.findPatientByPhoneNumber(anyString())).thenReturn(null);
        when(patientMapper.toPatient(any(PatientSaveDTO.class))).thenReturn(patient);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(patientMapper.toPatientDTO(any(Patient.class))).thenReturn(patientDTO);

        PatientDTO result = patientService.createPatient(patientSaveDTO);

        assertNotNull(result);
        assertEquals(patientDTO.getNik(), result.getNik());
        verify(userRepository).findById(patientSaveDTO.getUserId());
        verify(patientRepository).save(patient);
    }

    @Test
    public void testCreatePatientWithDuplicateNik() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(patientRepository.findPatientByNik(anyLong())).thenReturn(patient);

        DuplicateNikException thrown = assertThrows(
                DuplicateNikException.class,
                () -> patientService.createPatient(patientSaveDTO)
        );

        assertEquals("NIK already exists", thrown.getMessage());
        verify(userRepository).findById(patientSaveDTO.getUserId());
    }

    @Test
    public void testCreatePatientWithDuplicatePhoneNumber() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(patientRepository.findPatientByPhoneNumber(anyString())).thenReturn(patient);

        DuplicatePhoneNumberException thrown = assertThrows(
                DuplicatePhoneNumberException.class,
                () -> patientService.createPatient(patientSaveDTO)
        );

        assertEquals("Phone number already exists", thrown.getMessage());
        verify(userRepository).findById(patientSaveDTO.getUserId());
    }

    @Test
    public void testUpdatePatient() {
        when(patientRepository.findById(any(UUID.class))).thenReturn(Optional.of(patient));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(patientRepository.findPatientByNik(anyLong())).thenReturn(null);
        when(patientRepository.findPatientByPhoneNumber(anyString())).thenReturn(null);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(patientMapper.toPatientDTO(any(Patient.class))).thenReturn(patientDTO);

        PatientDTO result = patientService.updatePatient(patient.getId(), patientSaveDTO);

        assertNotNull(result);
        assertEquals(patientDTO.getNik(), result.getNik());
        verify(patientRepository).save(patient);
    }

    @Test
    public void testUpdatePatientWithDuplicateNik() {
        Patient existingPatientWithDifferentId = new Patient();
        existingPatientWithDifferentId.setId(UUID.randomUUID());
        when(patientRepository.findById(any(UUID.class))).thenReturn(Optional.of(patient));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(patientRepository.findPatientByNik(anyLong())).thenReturn(existingPatientWithDifferentId);

        DuplicateNikException thrown = assertThrows(
                DuplicateNikException.class,
                () -> patientService.updatePatient(patient.getId(), patientSaveDTO)
        );

        assertEquals("NIK already exists", thrown.getMessage());
        verify(patientRepository).findById(patient.getId());
    }

    @Test
    public void testUpdatePatientWithDuplicatePhoneNumber() {
        Patient existingPatientWithDifferentId = new Patient();
        existingPatientWithDifferentId.setId(UUID.randomUUID());
        when(patientRepository.findById(any(UUID.class))).thenReturn(Optional.of(patient));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(patientRepository.findPatientByPhoneNumber(anyString())).thenReturn(existingPatientWithDifferentId);

        DuplicatePhoneNumberException thrown = assertThrows(
                DuplicatePhoneNumberException.class,
                () -> patientService.updatePatient(patient.getId(), patientSaveDTO)
        );

        assertEquals("Phone number already exists", thrown.getMessage());
        verify(patientRepository).findById(patient.getId());
    }

    @Test
    public void testDeletePatient() {
        when(patientRepository.existsById(any(UUID.class))).thenReturn(true);

        assertDoesNotThrow(() -> patientService.deletePatient(patient.getId()));
        verify(patientRepository).deleteById(patient.getId());
    }

    @Test
    public void testDeletePatientNotFound() {
        when(patientRepository.existsById(any(UUID.class))).thenReturn(false);

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> patientService.deletePatient(patient.getId())
        );

        assertEquals("Patient Not Found", thrown.getMessage());
    }
}
