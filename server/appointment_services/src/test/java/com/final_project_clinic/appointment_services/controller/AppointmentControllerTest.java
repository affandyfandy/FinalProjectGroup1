package com.final_project_clinic.appointment_services.controller;

import com.final_project_clinic.appointment_services.dto.AppointmentCreateDTO;
import com.final_project_clinic.appointment_services.dto.AppointmentDTO;
import com.final_project_clinic.appointment_services.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AppointmentControllerTest {

    @InjectMocks
    private AppointmentController appointmentController;

    @Mock
    private AppointmentService appointmentService;

    private AppointmentDTO mockAppointmentDTO;
    private AppointmentCreateDTO mockAppointmentCreateDTO;
    private UUID mockAppointmentId;
    private UUID mockDoctorId;
    private UUID mockPatientId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockAppointmentId = UUID.randomUUID();
        mockDoctorId = UUID.randomUUID();
        mockPatientId = UUID.randomUUID();

        mockAppointmentDTO = AppointmentDTO.builder()
                .appointmentId(mockAppointmentId)
                .doctorId(mockDoctorId)
                .patientId(mockPatientId)
                .date(LocalDate.now())
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .queueNumber(1)
                .status("ONGOING")
                .build();

        mockAppointmentCreateDTO = new AppointmentCreateDTO();
        mockAppointmentCreateDTO.setDoctorId(mockDoctorId);
        mockAppointmentCreateDTO.setPatientId(mockPatientId);
        mockAppointmentCreateDTO.setDate(LocalDate.now());
        mockAppointmentCreateDTO.setStartTime(LocalTime.of(9, 0));
    }

    @Test
    public void testCreateAppointment() {
        when(appointmentService.createAppointment(any(AppointmentCreateDTO.class))).thenReturn(mockAppointmentDTO);

        ResponseEntity<AppointmentDTO> response = appointmentController.createAppointment(mockAppointmentCreateDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockAppointmentDTO, response.getBody());
        verify(appointmentService, times(1)).createAppointment(any(AppointmentCreateDTO.class));
    }

    @Test
    public void testGetAllAppointments() {
        when(appointmentService.getAllAppointments(0, 10)).thenReturn(Collections.singletonList(mockAppointmentDTO));

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAllAppointments(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(mockAppointmentDTO, response.getBody().get(0));
        verify(appointmentService, times(1)).getAllAppointments(0, 10);
    }

    @Test
    public void testGetAppointmentById() {
        when(appointmentService.getAppointmentById(mockAppointmentId)).thenReturn(mockAppointmentDTO);

        ResponseEntity<AppointmentDTO> response = appointmentController.getAppointmentById(mockAppointmentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAppointmentDTO, response.getBody());
        verify(appointmentService, times(1)).getAppointmentById(mockAppointmentId);
    }

    @Test
    public void testDeleteAppointment() {
        doNothing().when(appointmentService).deleteAppointment(mockAppointmentId);

        ResponseEntity<Void> response = appointmentController.deleteAppointment(mockAppointmentId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(appointmentService, times(1)).deleteAppointment(mockAppointmentId);
    }

    @Test
    public void testUpdateAppointment() {
        when(appointmentService.updateAppointment(eq(mockAppointmentId), any(AppointmentCreateDTO.class))).thenReturn(mockAppointmentDTO);

        ResponseEntity<AppointmentDTO> response = appointmentController.updateAppointment(mockAppointmentId, mockAppointmentCreateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAppointmentDTO, response.getBody());
        verify(appointmentService, times(1)).updateAppointment(eq(mockAppointmentId), any(AppointmentCreateDTO.class));
    }


    @Test
    public void testGetAppointmentsByDoctorId() {
        when(appointmentService.getAppointmentsByDoctorId(mockDoctorId, 0, 10)).thenReturn(Collections.singletonList(mockAppointmentDTO));

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAppointmentsByDoctorId(mockDoctorId, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(mockAppointmentDTO, response.getBody().get(0));
        verify(appointmentService, times(1)).getAppointmentsByDoctorId(mockDoctorId, 0, 10);
    }
}
