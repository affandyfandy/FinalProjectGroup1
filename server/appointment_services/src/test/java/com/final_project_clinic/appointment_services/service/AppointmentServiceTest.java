package com.final_project_clinic.appointment_services.service;

import com.final_project_clinic.appointment_services.client.DoctorServiceClient;
import com.final_project_clinic.appointment_services.client.UserServiceClient;
import com.final_project_clinic.appointment_services.data.model.Appointment;
import com.final_project_clinic.appointment_services.data.repository.AppointmentRepository;
import com.final_project_clinic.appointment_services.dto.*;
import com.final_project_clinic.appointment_services.exception.DuplicateAppointmentException;
import com.final_project_clinic.appointment_services.exception.MaxPatientExceededException;
import com.final_project_clinic.appointment_services.exception.ResourceNotFoundException;
import com.final_project_clinic.appointment_services.exception.ScheduleNotFoundException;
import com.final_project_clinic.appointment_services.mapper.AppointmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private DoctorServiceClient doctorServiceClient;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentService appointmentService;

    private AppointmentCreateDTO appointmentCreateDTO;
    private DoctorScheduleDTO doctorScheduleDTO;
    private ScheduleTimeDTO scheduleTimeDTO;
    private Appointment appointment;
    private AppointmentDTO appointmentDTO;
    private UserDTO patient;

    @BeforeEach
    public void setUp() {
        // Inisialisasi DTO dan entitas
        appointmentCreateDTO = AppointmentCreateDTO.builder()
                .doctorId(UUID.fromString("79245e23-22d1-447c-9697-6caa34fa103a"))
                .patientId(UUID.fromString("a56b6d45-b4fc-451f-b9c1-e9a0471a8e20"))
                .date(LocalDate.of(2024, 9, 25))
                .startTime(LocalTime.of(10, 0))
                .initialComplaint("Headache")
                .build();

        scheduleTimeDTO = ScheduleTimeDTO.builder()
                .startWorkingHour(LocalTime.of(10, 0))
                .endWorkingHour(LocalTime.of(11, 0))
                .maxPatient(5)
                .build();

        doctorScheduleDTO = DoctorScheduleDTO.builder()
                .scheduleTimes(Arrays.asList(scheduleTimeDTO))
                .build();

        appointment = Appointment.builder()
                .appointmentId(UUID.randomUUID())
                .doctorId(appointmentCreateDTO.getDoctorId())
                .patientId(appointmentCreateDTO.getPatientId())
                .initialComplaint(appointmentCreateDTO.getInitialComplaint())
                .date(appointmentCreateDTO.getDate())
                .startTime(appointmentCreateDTO.getStartTime())
                .endTime(scheduleTimeDTO.getEndWorkingHour())
                .queueNumber(1)
                .status("ONGOING")
                .build();

        appointmentDTO = AppointmentDTO.builder()
                .appointmentId(appointment.getAppointmentId())
                .doctorId(appointment.getDoctorId())
                .patientId(appointment.getPatientId())
                .initialComplaint(appointment.getInitialComplaint())
                .date(appointment.getDate())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .queueNumber(appointment.getQueueNumber())
                .status(appointment.getStatus())
                .build();

        patient = UserDTO.builder()
                .userId(appointmentCreateDTO.getPatientId())
                .fullName("John Doe")
                .build();
    }

    @Test
    public void testCreateAppointmentSuccess() {
        // Mocking dependencies
        when(doctorServiceClient.getDoctorScheduleByDay(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate().getDayOfWeek().name()))
                .thenReturn(doctorScheduleDTO);

        when(userServiceClient.getUserById(appointmentCreateDTO.getPatientId()))
                .thenReturn(patient);

        when(appointmentRepository.existsByDoctorIdAndDateAndStartTimeAndPatientId(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate(),
                appointmentCreateDTO.getStartTime(),
                appointmentCreateDTO.getPatientId()))
                .thenReturn(false);

        when(appointmentRepository.countByDoctorIdAndDateAndStartTime(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate(),
                appointmentCreateDTO.getStartTime()))
                .thenReturn(0L);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toAppointmentDTO(any(Appointment.class))).thenReturn(appointmentDTO);

        // Eksekusi metode yang diuji
        AppointmentDTO result = appointmentService.createAppointment(appointmentCreateDTO);

        // Verifikasi
        assertNotNull(result);
        assertEquals(1, result.getQueueNumber());
        assertEquals("Headache", result.getInitialComplaint());
        verify(doctorServiceClient, times(1)).getDoctorScheduleByDay(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate().getDayOfWeek().name());
        verify(userServiceClient, times(1)).getUserById(appointmentCreateDTO.getPatientId());
        verify(appointmentRepository, times(1)).existsByDoctorIdAndDateAndStartTimeAndPatientId(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate(),
                appointmentCreateDTO.getStartTime(),
                appointmentCreateDTO.getPatientId());
        verify(appointmentRepository, times(1)).countByDoctorIdAndDateAndStartTime(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate(),
                appointmentCreateDTO.getStartTime());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
        verify(appointmentMapper, times(1)).toAppointmentDTO(any(Appointment.class));
    }

    @Test
    public void testCreateAppointmentPatientNotFound() {
        // Mocking dependencies
        when(doctorServiceClient.getDoctorScheduleByDay(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate().getDayOfWeek().name()))
                .thenReturn(doctorScheduleDTO);

        when(userServiceClient.getUserById(appointmentCreateDTO.getPatientId()))
                .thenReturn(null);

        // Eksekusi dan verifikasi exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.createAppointment(appointmentCreateDTO);
        });

        assertEquals("Patient not found with ID: " + appointmentCreateDTO.getPatientId(), exception.getMessage());

        verify(doctorServiceClient, times(1)).getDoctorScheduleByDay(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate().getDayOfWeek().name());
        verify(userServiceClient, times(1)).getUserById(appointmentCreateDTO.getPatientId());
        verifyNoMoreInteractions(appointmentRepository);
        verifyNoMoreInteractions(appointmentMapper);
    }

    @Test
    public void testCreateAppointmentScheduleNotFound() {
        // Mocking dependencies
        when(doctorServiceClient.getDoctorScheduleByDay(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate().getDayOfWeek().name()))
                .thenReturn(null);

        // Eksekusi dan verifikasi exception
        ScheduleNotFoundException exception = assertThrows(ScheduleNotFoundException.class, () -> {
            appointmentService.createAppointment(appointmentCreateDTO);
        });

        assertEquals("Schedule not found for the specified doctor and day.", exception.getMessage());

        verify(doctorServiceClient, times(1)).getDoctorScheduleByDay(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate().getDayOfWeek().name());
        verifyNoMoreInteractions(userServiceClient);
        verifyNoMoreInteractions(appointmentRepository);
        verifyNoMoreInteractions(appointmentMapper);
    }

    @Test
    public void testCreateAppointmentNoScheduleTime() {
        // Mocking dependencies
        DoctorScheduleDTO scheduleWithoutMatchingTime = DoctorScheduleDTO.builder()
                .scheduleTimes(Arrays.asList(
                        ScheduleTimeDTO.builder()
                                .startWorkingHour(LocalTime.of(9, 0))
                                .endWorkingHour(LocalTime.of(10, 0))
                                .maxPatient(5)
                                .build()
                ))
                .build();

        when(doctorServiceClient.getDoctorScheduleByDay(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate().getDayOfWeek().name()))
                .thenReturn(scheduleWithoutMatchingTime);

        when(userServiceClient.getUserById(appointmentCreateDTO.getPatientId()))
                .thenReturn(patient);

        // Eksekusi dan verifikasi exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.createAppointment(appointmentCreateDTO);
        });

        assertEquals("No schedule found for the provided start working hour.", exception.getMessage());

        verify(doctorServiceClient, times(1)).getDoctorScheduleByDay(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate().getDayOfWeek().name());
        verify(userServiceClient, times(1)).getUserById(appointmentCreateDTO.getPatientId());
        verifyNoMoreInteractions(appointmentRepository);
        verifyNoMoreInteractions(appointmentMapper);
    }

    @Test
    public void testCreateAppointmentDuplicateAppointment() {
        // Mocking dependencies
        when(doctorServiceClient.getDoctorScheduleByDay(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate().getDayOfWeek().name()))
                .thenReturn(doctorScheduleDTO);

        when(userServiceClient.getUserById(appointmentCreateDTO.getPatientId()))
                .thenReturn(patient);

        when(appointmentRepository.existsByDoctorIdAndDateAndStartTimeAndPatientId(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate(),
                appointmentCreateDTO.getStartTime(),
                appointmentCreateDTO.getPatientId()))
                .thenReturn(true);

        // Eksekusi dan verifikasi exception
        DuplicateAppointmentException exception = assertThrows(DuplicateAppointmentException.class, () -> {
            appointmentService.createAppointment(appointmentCreateDTO);
        });

        assertEquals("You already have an appointment with this doctor at the same time.", exception.getMessage());

        verify(doctorServiceClient, times(1)).getDoctorScheduleByDay(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate().getDayOfWeek().name());
        verify(userServiceClient, times(1)).getUserById(appointmentCreateDTO.getPatientId());
        verify(appointmentRepository, times(1)).existsByDoctorIdAndDateAndStartTimeAndPatientId(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate(),
                appointmentCreateDTO.getStartTime(),
                appointmentCreateDTO.getPatientId());
        verifyNoMoreInteractions(appointmentRepository);
        verifyNoMoreInteractions(appointmentMapper);
    }

    @Test
    public void testCreateAppointmentMaxPatientExceeded() {
        // Mocking dependencies
        when(doctorServiceClient.getDoctorScheduleByDay(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate().getDayOfWeek().name()))
                .thenReturn(doctorScheduleDTO);

        when(userServiceClient.getUserById(appointmentCreateDTO.getPatientId()))
                .thenReturn(patient);

        when(appointmentRepository.existsByDoctorIdAndDateAndStartTimeAndPatientId(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate(),
                appointmentCreateDTO.getStartTime(),
                appointmentCreateDTO.getPatientId()))
                .thenReturn(false);

        when(appointmentRepository.countByDoctorIdAndDateAndStartTime(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate(),
                appointmentCreateDTO.getStartTime()))
                .thenReturn(5L); // Sama dengan maxPatient

        // Eksekusi dan verifikasi exception
        MaxPatientExceededException exception = assertThrows(MaxPatientExceededException.class, () -> {
            appointmentService.createAppointment(appointmentCreateDTO);
        });

        assertEquals("Max patient limit exceeded for this time slot.", exception.getMessage());

        verify(doctorServiceClient, times(1)).getDoctorScheduleByDay(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate().getDayOfWeek().name());
        verify(userServiceClient, times(1)).getUserById(appointmentCreateDTO.getPatientId());
        verify(appointmentRepository, times(1)).existsByDoctorIdAndDateAndStartTimeAndPatientId(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate(),
                appointmentCreateDTO.getStartTime(),
                appointmentCreateDTO.getPatientId());
        verify(appointmentRepository, times(1)).countByDoctorIdAndDateAndStartTime(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate(),
                appointmentCreateDTO.getStartTime());
        verifyNoMoreInteractions(appointmentRepository);
        verifyNoMoreInteractions(appointmentMapper);
    }

    @Test
    public void testCancelAppointmentSuccess() {
        UUID appointmentId = appointment.getAppointmentId();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toAppointmentDTO(any(Appointment.class))).thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.cancelAppointment(appointmentId);

        assertNotNull(result);
        assertEquals("CANCEL", appointment.getStatus());

        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, times(1)).save(appointment);
        verify(appointmentMapper, times(1)).toAppointmentDTO(appointment);
    }

    @Test
    public void testCancelAppointmentNotFound() {
        UUID appointmentId = appointment.getAppointmentId();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.cancelAppointment(appointmentId);
        });

        assertEquals("Appointment not found with id: " + appointmentId, exception.getMessage());
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verifyNoMoreInteractions(appointmentRepository);
        verifyNoMoreInteractions(appointmentMapper);
    }

    @Test
    public void testUpdateAppointmentSuccess() {
        UUID appointmentId = appointment.getAppointmentId();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toAppointmentDTO(any(Appointment.class))).thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.updateAppointment(appointmentId, appointmentCreateDTO);

        assertNotNull(result);
        assertEquals("UPDATED", appointment.getStatus());
        assertEquals(appointmentCreateDTO.getDate(), appointment.getDate());
        assertEquals(appointmentCreateDTO.getStartTime(), appointment.getStartTime());
        assertEquals(appointmentCreateDTO.getDoctorId(), appointment.getDoctorId());
        assertEquals(appointmentCreateDTO.getPatientId(), appointment.getPatientId());
        assertEquals(appointmentCreateDTO.getInitialComplaint(), appointment.getInitialComplaint());

        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, times(1)).save(appointment);
        verify(appointmentMapper, times(1)).toAppointmentDTO(appointment);
    }

    @Test
    public void testUpdateAppointmentNotFound() {
        UUID appointmentId = appointment.getAppointmentId();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.updateAppointment(appointmentId, appointmentCreateDTO);
        });

        assertEquals("Appointment not found with id: " + appointmentId, exception.getMessage());
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verifyNoMoreInteractions(appointmentRepository);
        verifyNoMoreInteractions(appointmentMapper);
    }

    @Test
    public void testDeleteAppointmentSuccess() {
        UUID appointmentId = appointment.getAppointmentId();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        appointmentService.deleteAppointment(appointmentId);

        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, times(1)).delete(appointment);
    }

    @Test
    public void testDeleteAppointmentNotFound() {
        UUID appointmentId = appointment.getAppointmentId();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.deleteAppointment(appointmentId);
        });

        assertEquals("Appointment not found with id: " + appointmentId, exception.getMessage());
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verifyNoMoreInteractions(appointmentRepository);
    }

    @Test
    public void testGetAppointmentByIdSuccess() {
        UUID appointmentId = appointment.getAppointmentId();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toAppointmentDTO(any(Appointment.class))).thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.getAppointmentById(appointmentId);

        assertNotNull(result);
        assertEquals(appointmentId, result.getAppointmentId());
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentMapper, times(1)).toAppointmentDTO(appointment);
    }

    @Test
    public void testGetAppointmentByIdNotFound() {
        UUID appointmentId = appointment.getAppointmentId();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.getAppointmentById(appointmentId);
        });

        assertEquals("Appointment not found with id: " + appointmentId, exception.getMessage());
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verifyNoMoreInteractions(appointmentMapper);
    }

}
