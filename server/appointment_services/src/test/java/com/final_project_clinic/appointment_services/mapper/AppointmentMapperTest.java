package com.final_project_clinic.appointment_services.mapper;

import com.final_project_clinic.appointment_services.data.model.Appointment;
import com.final_project_clinic.appointment_services.dto.AppointmentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppointmentMapperTest {

    private AppointmentMapper appointmentMapper;

    @BeforeEach
    public void setUp() {
        appointmentMapper = Mappers.getMapper(AppointmentMapper.class);
    }

    @Test
    public void testToAppointmentDTO() {
        // Given
        Appointment appointment = Appointment.builder()
                .appointmentId(UUID.randomUUID())
                .doctorId(UUID.randomUUID())
                .patientId(UUID.randomUUID())
                .date(LocalDate.now())
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .queueNumber(1)
                .status("ONGOING")
                .initialComplaint("Headache")
                .build();

        // When
        AppointmentDTO appointmentDTO = appointmentMapper.toAppointmentDTO(appointment);

        // Then
        assertNotNull(appointmentDTO);
        assertEquals(appointment.getAppointmentId(), appointmentDTO.getAppointmentId());
        assertEquals(appointment.getDoctorId(), appointmentDTO.getDoctorId());
        assertEquals(appointment.getPatientId(), appointmentDTO.getPatientId());
        assertEquals(appointment.getDate(), appointmentDTO.getDate());
        assertEquals(appointment.getStartTime(), appointmentDTO.getStartTime());
        assertEquals(appointment.getEndTime(), appointmentDTO.getEndTime());
        assertEquals(appointment.getQueueNumber(), appointmentDTO.getQueueNumber());
        assertEquals(appointment.getStatus(), appointmentDTO.getStatus());
        assertEquals(appointment.getInitialComplaint(), appointmentDTO.getInitialComplaint());
    }

    @Test
    public void testToAppointment() {
        // Given
        AppointmentDTO appointmentDTO = AppointmentDTO.builder()
                .appointmentId(UUID.randomUUID())
                .doctorId(UUID.randomUUID())
                .patientId(UUID.randomUUID())
                .date(LocalDate.now())
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .queueNumber(1)
                .status("ONGOING")
                .build();

        // When
        Appointment appointment = appointmentMapper.toAppointment(appointmentDTO);

        // Then
        assertNotNull(appointment);
        assertEquals(appointmentDTO.getAppointmentId(), appointment.getAppointmentId());
        assertEquals(appointmentDTO.getDoctorId(), appointment.getDoctorId());
        assertEquals(appointmentDTO.getPatientId(), appointment.getPatientId());
        assertEquals(appointmentDTO.getDate(), appointment.getDate());
        assertEquals(appointmentDTO.getStartTime(), appointment.getStartTime());
        assertEquals(appointmentDTO.getEndTime(), appointment.getEndTime());
        assertEquals(appointmentDTO.getQueueNumber(), appointment.getQueueNumber());
        assertEquals(appointmentDTO.getStatus(), appointment.getStatus());
    }
}
