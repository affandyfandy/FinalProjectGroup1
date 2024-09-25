package com.final_project_clinic.doctor.controller;

import com.final_project_clinic.doctor.dto.DoctorScheduleDTO;
import com.final_project_clinic.doctor.dto.ScheduleTimeDTO;
import com.final_project_clinic.doctor.service.DoctorScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DoctorScheduleControllerTest {

    @Mock
    private DoctorScheduleService doctorScheduleService;

    @InjectMocks
    private DoctorScheduleController doctorScheduleController;

    private DoctorScheduleDTO doctorScheduleDTO;
    private UUID scheduleId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        scheduleId = UUID.randomUUID();
        doctorScheduleDTO = new DoctorScheduleDTO(
                scheduleId,
                UUID.randomUUID(),
                "Monday",
                Arrays.asList(new ScheduleTimeDTO(LocalTime.of(9, 0), LocalTime.of(17, 0), 10, null, null, null, null))
        );
    }

    @Test
    void testGetAllSchedules() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<DoctorScheduleDTO> schedules = new PageImpl<>(Arrays.asList(doctorScheduleDTO));

        when(doctorScheduleService.getAllSchedules(pageable)).thenReturn(schedules);

        ResponseEntity<Page<DoctorScheduleDTO>> response = doctorScheduleController.getAllSchedules(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(schedules, response.getBody());
        verify(doctorScheduleService, times(1)).getAllSchedules(pageable);
    }

    @Test
    void testGetAllSchedulesList() {
        List<DoctorScheduleDTO> scheduleList = Arrays.asList(doctorScheduleDTO);
        when(doctorScheduleService.getAllSchedulesList()).thenReturn(scheduleList);

        ResponseEntity<List<DoctorScheduleDTO>> response = doctorScheduleController.getAllSchedulesList();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scheduleList, response.getBody());
        verify(doctorScheduleService, times(1)).getAllSchedulesList();
    }

    @Test
    void testGetScheduleById_Found() {
        when(doctorScheduleService.getScheduleById(scheduleId)).thenReturn(Optional.of(doctorScheduleDTO));

        ResponseEntity<DoctorScheduleDTO> response = doctorScheduleController.getScheduleById(scheduleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctorScheduleDTO, response.getBody());
        verify(doctorScheduleService, times(1)).getScheduleById(scheduleId);
    }

    @Test
    void testGetScheduleById_NotFound() {
        when(doctorScheduleService.getScheduleById(scheduleId)).thenReturn(Optional.empty());

        ResponseEntity<DoctorScheduleDTO> response = doctorScheduleController.getScheduleById(scheduleId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(doctorScheduleService, times(1)).getScheduleById(scheduleId);
    }

    @Test
    void testCreateSchedule() {
        List<DoctorScheduleDTO> scheduleDTOs = Arrays.asList(doctorScheduleDTO);
        when(doctorScheduleService.createSchedule(any())).thenReturn(doctorScheduleDTO);

        ResponseEntity<List<DoctorScheduleDTO>> response = doctorScheduleController.createSchedule(scheduleDTOs);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(doctorScheduleService, times(1)).createSchedule(any());
    }

    @Test
    void testUpdateScheduleTime() {
        ScheduleTimeDTO scheduleTimeDTO = new ScheduleTimeDTO(
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                10,
                null,
                null,
                null,
                null
        );
        LocalTime startWorkingHour = LocalTime.of(9, 0);
        when(doctorScheduleService.editScheduleTime(scheduleId, startWorkingHour, scheduleTimeDTO))
                .thenReturn(doctorScheduleDTO);

        ResponseEntity<DoctorScheduleDTO> response = doctorScheduleController.updateScheduleTime(
                scheduleId,
                startWorkingHour.toString(),
                scheduleTimeDTO
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctorScheduleDTO, response.getBody());
        verify(doctorScheduleService, times(1)).editScheduleTime(scheduleId, startWorkingHour, scheduleTimeDTO);
    }

    @Test
    void testDeleteSchedule_Found() {
        when(doctorScheduleService.getScheduleById(scheduleId)).thenReturn(Optional.of(doctorScheduleDTO));

        ResponseEntity<Object> response = doctorScheduleController.deleteSchedule(scheduleId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorScheduleService, times(1)).deleteSchedule(scheduleId);
    }

    @Test
    void testDeleteSchedule_NotFound() {
        when(doctorScheduleService.getScheduleById(scheduleId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = doctorScheduleController.deleteSchedule(scheduleId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(doctorScheduleService, times(0)).deleteSchedule(scheduleId);  // No delete if schedule not found
    }

//    @Test
//    void testDeleteScheduleTime() {
//        LocalTime startWorkingHour = LocalTime.of(9, 0);
//
//        ResponseEntity<Void> response = doctorScheduleController.deleteScheduleTime(scheduleId, startWorkingHour.toString());
//
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        verify(doctorScheduleService, times(1)).deleteScheduleTime(scheduleId, startWorkingHour);
//    }
}