package com.final_project_clinic.appointment_services.service;

import com.final_project_clinic.appointment_services.client.DoctorServiceClient;
import com.final_project_clinic.appointment_services.client.UserServiceClient;
import com.final_project_clinic.appointment_services.dto.AppointmentCreateDTO;
import com.final_project_clinic.appointment_services.dto.AppointmentDTO;
import com.final_project_clinic.appointment_services.dto.DoctorScheduleDTO;
import com.final_project_clinic.appointment_services.dto.ScheduleTimeDTO;
import com.final_project_clinic.appointment_services.dto.UserDTO;
import com.final_project_clinic.appointment_services.exception.DuplicateAppointmentException;
import com.final_project_clinic.appointment_services.exception.MaxPatientExceededException;
import com.final_project_clinic.appointment_services.exception.ResourceNotFoundException;
import com.final_project_clinic.appointment_services.exception.ScheduleNotFoundException;
import com.final_project_clinic.appointment_services.mapper.AppointmentMapper;
import com.final_project_clinic.appointment_services.data.model.Appointment;
import com.final_project_clinic.appointment_services.data.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorServiceClient doctorServiceClient;
    private final UserServiceClient userServiceClient;
    private final AppointmentMapper appointmentMapper;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
            DoctorServiceClient doctorServiceClient,
            UserServiceClient userServiceClient,
            AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.doctorServiceClient = doctorServiceClient;
        this.userServiceClient = userServiceClient;
        this.appointmentMapper = appointmentMapper;
    }

    public AppointmentDTO createAppointment(AppointmentCreateDTO appointmentCreateDTO) {
        // Mengambil jadwal dokter berdasarkan hari
        DoctorScheduleDTO doctorSchedule = doctorServiceClient.getDoctorScheduleByDay(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate().getDayOfWeek().name());

        if (doctorSchedule == null) {
            throw new ScheduleNotFoundException("Schedule not found for the specified doctor and day.");
        }

        // Check if the patient exists
        UserDTO patient = userServiceClient.getUserById(appointmentCreateDTO.getPatientId());
        if (patient == null) {
            throw new ResourceNotFoundException("Patient not found with ID: " + appointmentCreateDTO.getPatientId());
        }

        // Mencari waktu yang sesuai berdasarkan startWorkingHour
        Optional<ScheduleTimeDTO> matchingTime = doctorSchedule.getScheduleTimes().stream()
                .filter(time -> time.getStartWorkingHour().equals(appointmentCreateDTO.getStartTime()))
                .findFirst();

        if (matchingTime.isEmpty()) {
            throw new ResourceNotFoundException("No schedule found for the provided start working hour.");
        }

        // Mengecek apakah user yang sama sudah memiliki janji dengan dokter yang sama
        // di jam yang sama
        boolean userHasAppointment = appointmentRepository.existsByDoctorIdAndDateAndStartTimeAndPatientId(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate(),
                appointmentCreateDTO.getStartTime(),
                appointmentCreateDTO.getPatientId());

        if (userHasAppointment) {
            throw new DuplicateAppointmentException(
                    "You already have an appointment with this doctor at the same time.");
        }

        // Menghitung jumlah janji temu yang sudah ada pada kombinasi dokter, tanggal,
        // dan waktu
        long existingAppointmentsCount = appointmentRepository.countByDoctorIdAndDateAndStartTime(
                appointmentCreateDTO.getDoctorId(),
                appointmentCreateDTO.getDate(),
                appointmentCreateDTO.getStartTime());

        // Mengecek apakah jumlah janji temu melebihi batas maksimal pasien
        if (existingAppointmentsCount >= matchingTime.get().getMaxPatient()) {
            throw new MaxPatientExceededException("Max patient limit exceeded for this time slot.");
        }

        // Menghitung queue_number yang unik berdasarkan doctorId, date, dan startTime
        Integer queueNumber = (int) existingAppointmentsCount + 1;

        // Membuat entitas appointment baru
        Appointment appointment = Appointment.builder()
                .doctorId(appointmentCreateDTO.getDoctorId())
                .patientId(appointmentCreateDTO.getPatientId())
                .initialComplaint(appointmentCreateDTO.getInitialComplaint())
                .date(appointmentCreateDTO.getDate())
                .startTime(appointmentCreateDTO.getStartTime())
                .endTime(matchingTime.get().getEndWorkingHour()) // Set endTime dari jadwal
                .queueNumber(queueNumber) // Set queueNumber yang berulang per doctorId, date, dan startTime
                .status("ONGOING")
                .build();

        // Menyimpan janji temu ke database
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.toAppointmentDTO(savedAppointment);
    }

    public List<AppointmentDTO> getAllAppointments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> appointmentsPage = appointmentRepository.findAll(pageable);
        return appointmentsPage.stream()
                .map(appointmentMapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAllAppointmentsList() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(appointmentMapper::toAppointmentDTO)
                .toList();
    }

    public List<AppointmentDTO> getAppointmentsByPatientId(UUID patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> appointmentsPage = appointmentRepository.findByPatientId(patientId, pageable);
        return appointmentsPage.stream()
                .map(appointmentMapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAppointmentsByDoctorId(UUID doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> appointmentsPage = appointmentRepository.findByDoctorId(doctorId, pageable);
        return appointmentsPage.stream()
                .map(appointmentMapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    public AppointmentDTO updateAppointment(UUID id, AppointmentCreateDTO appointmentCreateDTO) {
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        // Update the fields
        existingAppointment.setDate(appointmentCreateDTO.getDate());
        existingAppointment.setStartTime(appointmentCreateDTO.getStartTime());
        existingAppointment.setDoctorId(appointmentCreateDTO.getDoctorId());
        existingAppointment.setPatientId(appointmentCreateDTO.getPatientId());
        existingAppointment.setStatus("UPDATED");

        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        return appointmentMapper.toAppointmentDTO(updatedAppointment);
    }

    public void deleteAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        appointmentRepository.delete(appointment);
    }

    public AppointmentDTO cancelAppointment(UUID id) {
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        existingAppointment.setStatus("CANCEL");
        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        return appointmentMapper.toAppointmentDTO(updatedAppointment);
    }

    public AppointmentDTO getAppointmentById(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        return appointmentMapper.toAppointmentDTO(appointment);
    }
}
