package com.final_project_clinic.doctor.service;

import com.final_project_clinic.doctor.data.model.Doctor;
import com.final_project_clinic.doctor.data.model.DoctorSchedule;
import com.final_project_clinic.doctor.data.model.ScheduleTime;
import com.final_project_clinic.doctor.data.repository.DoctorRepository;
import com.final_project_clinic.doctor.data.repository.DoctorScheduleRepository;
import com.final_project_clinic.doctor.data.repository.ScheduleTimeRepository;
import com.final_project_clinic.doctor.dto.DoctorDTO;
import com.final_project_clinic.doctor.dto.DoctorScheduleDTO;
import com.final_project_clinic.doctor.dto.ScheduleTimeDTO;
import com.final_project_clinic.doctor.mapper.DoctorScheduleMapper;
import com.final_project_clinic.doctor.service.impl.DoctorScheduleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorScheduleServiceImplTest {

    @InjectMocks
    private DoctorScheduleServiceImpl doctorScheduleService;

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @Mock
    private ScheduleTimeRepository scheduleTimeRepository;

    @Mock
    private DoctorScheduleMapper doctorScheduleMapper;

    @Mock
    private DoctorRepository doctorRepository;


    @Test
    void getAllSchedules_ReturnsSchedulesPageTest() {
        DoctorSchedule doctorSchedule = new DoctorSchedule();
        DoctorScheduleDTO doctorScheduleDTO = new DoctorScheduleDTO();
        Pageable pageable = PageRequest.of(0, 10);
        Page<DoctorSchedule> doctorSchedules = new PageImpl<>(Collections.singletonList(doctorSchedule));

        when(doctorScheduleRepository.findAll(pageable)).thenReturn(doctorSchedules);
        when(doctorScheduleMapper.toDoctorScheduleDTO(doctorSchedule)).thenReturn(doctorScheduleDTO);

        Page<DoctorScheduleDTO> result = doctorScheduleService.getAllSchedules(pageable);

        assertEquals(1, result.getTotalElements());

        verify(doctorScheduleRepository, times(1)).findAll(pageable);
        verify(doctorScheduleMapper, times(1)).toDoctorScheduleDTO(doctorSchedule);
    }

    @Test
    void getAllSchedulesList_ReturnsSchedulesListTest() {
        DoctorSchedule doctorSchedule = new DoctorSchedule();
        DoctorScheduleDTO doctorScheduleDTO = new DoctorScheduleDTO();

        when(doctorScheduleRepository.findAll()).thenReturn(Collections.singletonList(doctorSchedule));
        when(doctorScheduleMapper.toDoctorScheduleDTO(doctorSchedule)).thenReturn(doctorScheduleDTO);

        var result = doctorScheduleService.getAllSchedulesList();

        assertEquals(1, result.size());

        verify(doctorScheduleRepository, times(1)).findAll();
        verify(doctorScheduleMapper, times(1)).toDoctorScheduleDTO(doctorSchedule);
    }

    @Test
    void getScheduleById_ReturnsScheduleTest() {
        UUID id = UUID.randomUUID();
        DoctorSchedule doctorSchedule = new DoctorSchedule();
        DoctorScheduleDTO doctorScheduleDTO = new DoctorScheduleDTO();

        when(doctorScheduleRepository.findById(id)).thenReturn(Optional.of(doctorSchedule));
        when(doctorScheduleMapper.toDoctorScheduleDTO(doctorSchedule)).thenReturn(doctorScheduleDTO);

        Optional<DoctorScheduleDTO> result = doctorScheduleService.getScheduleById(id);

        assertTrue(result.isPresent());

        verify(doctorScheduleRepository, times(1)).findById(id);
        verify(doctorScheduleMapper, times(1)).toDoctorScheduleDTO(doctorSchedule);
    }

    @Test
    void createSchedule_SavesAndReturnsScheduleTest() {
        DoctorScheduleDTO doctorScheduleDTO = new DoctorScheduleDTO();
        doctorScheduleDTO.setDoctorId(UUID.randomUUID());
        doctorScheduleDTO.setDay("Monday");

        doctorScheduleDTO.setScheduleTimes(new ArrayList<>());

        Doctor doctor = new Doctor();
        doctor.setId(doctorScheduleDTO.getDoctorId());

        DoctorSchedule doctorSchedule = new DoctorSchedule();
        doctorSchedule.setDoctor(doctor);
        doctorSchedule.setDay("Monday");

        when(doctorRepository.findById(doctorScheduleDTO.getDoctorId())).thenReturn(Optional.of(doctor));
        when(doctorScheduleRepository.findByDoctorIdAndDay(doctor.getId(), "Monday")).thenReturn(Optional.empty());
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenReturn(doctorSchedule);
        when(doctorScheduleMapper.toDoctorScheduleDTO(doctorSchedule)).thenReturn(doctorScheduleDTO);

        DoctorScheduleDTO result = doctorScheduleService.createSchedule(doctorScheduleDTO);

        assertEquals(doctorScheduleDTO, result);
        verify(doctorRepository, times(1)).findById(doctorScheduleDTO.getDoctorId());
        verify(doctorScheduleRepository, times(1)).findByDoctorIdAndDay(doctor.getId(), "Monday");
        verify(doctorScheduleRepository, times(1)).save(any(DoctorSchedule.class));
        verify(doctorScheduleMapper, times(1)).toDoctorScheduleDTO(doctorSchedule);
    }

    @Test
    void editScheduleTime_UpdatesAndReturnsScheduleTimeTest() {
        UUID scheduleId = UUID.randomUUID();
        LocalTime startWorkingHour = LocalTime.of(9, 0);
        ScheduleTimeDTO scheduleTimeDTO = new ScheduleTimeDTO();
        scheduleTimeDTO.setEndWorkingHour(LocalTime.of(17, 0));
        scheduleTimeDTO.setMaxPatient(10);

        DoctorSchedule doctorSchedule = new DoctorSchedule();
        ScheduleTime existingScheduleTime = new ScheduleTime();
        existingScheduleTime.setStartWorkingHour(startWorkingHour);

        List<ScheduleTime> scheduleTimes = new ArrayList<>();
        scheduleTimes.add(existingScheduleTime);
        doctorSchedule.setScheduleTimes(scheduleTimes);

        when(doctorScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(doctorSchedule));
        when(scheduleTimeRepository.save(existingScheduleTime)).thenReturn(existingScheduleTime);
        when(doctorScheduleMapper.toDoctorScheduleDTO(doctorSchedule)).thenReturn(new DoctorScheduleDTO());

        DoctorScheduleDTO result = doctorScheduleService.editScheduleTime(scheduleId, startWorkingHour, scheduleTimeDTO);

        assertNotNull(result);
        assertEquals(scheduleTimeDTO.getEndWorkingHour(), existingScheduleTime.getEndWorkingHour());
        assertEquals(scheduleTimeDTO.getMaxPatient(), existingScheduleTime.getMaxPatient());

        verify(doctorScheduleRepository, times(1)).findById(scheduleId);
        verify(scheduleTimeRepository, times(1)).save(existingScheduleTime);
        verify(doctorScheduleMapper, times(1)).toDoctorScheduleDTO(doctorSchedule);
    }

    @Test
    void deleteSchedule_DeletesScheduleTest() {
        UUID id = UUID.randomUUID();

        doNothing().when(doctorScheduleRepository).deleteById(id);

        doctorScheduleService.deleteSchedule(id);

        verify(doctorScheduleRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteScheduleTime_RemovesScheduleTimeTest() {
        UUID scheduleId = UUID.randomUUID();
        LocalTime startWorkingHour = LocalTime.of(9, 0);

        DoctorSchedule doctorSchedule = new DoctorSchedule();
        ScheduleTime scheduleTime = new ScheduleTime();
        scheduleTime.setStartWorkingHour(startWorkingHour);

        List<ScheduleTime> scheduleTimes = new ArrayList<>();
        scheduleTimes.add(scheduleTime);
        doctorSchedule.setScheduleTimes(scheduleTimes);

        when(doctorScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(doctorSchedule));

        doctorScheduleService.deleteScheduleTime(scheduleId, startWorkingHour);

        verify(doctorScheduleRepository, times(1)).findById(scheduleId);
        verify(scheduleTimeRepository, times(1)).delete(scheduleTime);
    }

    @Test
    void createSchedule_ThrowsExceptionWhenDoctorNotFoundTest() {
        DoctorScheduleDTO doctorScheduleDTO = new DoctorScheduleDTO();
        doctorScheduleDTO.setDoctorId(UUID.randomUUID());

        when(doctorRepository.findById(doctorScheduleDTO.getDoctorId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                doctorScheduleService.createSchedule(doctorScheduleDTO));

        assertEquals("Doctor not found", exception.getMessage());
        verify(doctorRepository, times(1)).findById(doctorScheduleDTO.getDoctorId());
    }

    @Test
    void editScheduleTime_ThrowsExceptionWhenScheduleNotFoundTest() {
        UUID scheduleId = UUID.randomUUID();
        LocalTime startWorkingHour = LocalTime.of(9, 0);
        ScheduleTimeDTO scheduleTimeDTO = new ScheduleTimeDTO();

        when(doctorScheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                doctorScheduleService.editScheduleTime(scheduleId, startWorkingHour, scheduleTimeDTO));

        assertEquals("Schedule not found", exception.getMessage());
        verify(doctorScheduleRepository, times(1)).findById(scheduleId);
    }

    @Test
    void deleteScheduleTime_ThrowsExceptionWhenScheduleNotFoundTest() {
        UUID scheduleId = UUID.randomUUID();
        LocalTime startWorkingHour = LocalTime.of(9, 0);

        when(doctorScheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                doctorScheduleService.deleteScheduleTime(scheduleId, startWorkingHour));

        assertEquals("Schedule not found", exception.getMessage());
        verify(doctorScheduleRepository, times(1)).findById(scheduleId);
    }
}
