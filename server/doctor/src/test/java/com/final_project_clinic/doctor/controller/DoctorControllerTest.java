package com.final_project_clinic.doctor.controller;

import com.final_project_clinic.doctor.dto.DoctorDTO;
import com.final_project_clinic.doctor.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    private DoctorDTO doctorDTO;
    private UUID doctorId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doctorId = UUID.randomUUID();
        doctorDTO = new DoctorDTO(
                doctorId,
                "Doctor A",
                "General",
                "236789",
                "+6283929273729",
                "Male",
                LocalDate.of(2004, 11, 27),
                "SBH",
                20,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "admin@gmail.com",
                "admin@gmail.com"
        );
    }

    @Test
    void testGetAllDoctors() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<DoctorDTO> doctors = new PageImpl<>(Arrays.asList(doctorDTO));

        when(doctorService.getAllDoctors(pageable)).thenReturn(doctors);

        ResponseEntity<Page<DoctorDTO>> response = doctorController.getAllDoctors(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctors, response.getBody());
        verify(doctorService, times(1)).getAllDoctors(pageable);
    }

    @Test
    void testGetAllDoctorsList() {
        List<DoctorDTO> doctorList = Arrays.asList(doctorDTO);
        when(doctorService.getAllDoctorsList()).thenReturn(doctorList);

        ResponseEntity<List<DoctorDTO>> response = doctorController.getAllDoctorsList();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctorList, response.getBody());
        verify(doctorService, times(1)).getAllDoctorsList();
    }

    @Test
    void testGetDoctorById_Found() {
        when(doctorService.getDoctorById(doctorId)).thenReturn(Optional.of(doctorDTO));

        ResponseEntity<DoctorDTO> response = doctorController.getDoctorById(doctorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctorDTO, response.getBody());
        verify(doctorService, times(1)).getDoctorById(doctorId);
    }

    @Test
    void testGetDoctorById_NotFound() {
        when(doctorService.getDoctorById(doctorId)).thenReturn(Optional.empty());

        ResponseEntity<DoctorDTO> response = doctorController.getDoctorById(doctorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(doctorService, times(1)).getDoctorById(doctorId);
    }

    @Test
    void testCreateDoctor() {
        when(doctorService.createDoctor(doctorDTO)).thenReturn(doctorDTO);

        ResponseEntity<DoctorDTO> response = doctorController.createDoctor(doctorDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(doctorDTO, response.getBody());
        verify(doctorService, times(1)).createDoctor(doctorDTO);
    }

    @Test
    void testEditDoctor_Found() {
        when(doctorService.editDoctor(doctorId, doctorDTO)).thenReturn(Optional.of(doctorDTO));

        ResponseEntity<DoctorDTO> response = doctorController.editDoctor(doctorId, doctorDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctorDTO, response.getBody());
        verify(doctorService, times(1)).editDoctor(doctorId, doctorDTO);
    }

    @Test
    void testEditDoctor_NotFound() {
        when(doctorService.editDoctor(doctorId, doctorDTO)).thenReturn(Optional.empty());

        ResponseEntity<DoctorDTO> response = doctorController.editDoctor(doctorId, doctorDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(doctorService, times(1)).editDoctor(doctorId, doctorDTO);
    }

    @Test
    void testDeleteDoctor_Found() {
        when(doctorService.getDoctorById(doctorId)).thenReturn(Optional.of(doctorDTO));

        ResponseEntity<Object> response = doctorController.deleteDoctor(doctorId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorService, times(1)).deleteDoctor(doctorId);
    }

    @Test
    void testDeleteDoctor_NotFound() {
        when(doctorService.getDoctorById(doctorId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = doctorController.deleteDoctor(doctorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(doctorService, times(0)).deleteDoctor(doctorId);
    }
}