package com.final_project_clinic.doctor.service;

import com.final_project_clinic.doctor.dto.DoctorScheduleDTO;
import com.final_project_clinic.doctor.dto.ScheduleTimeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface DoctorScheduleService {

    Page<DoctorScheduleDTO> getAllSchedules(Pageable pageable);

    List<DoctorScheduleDTO> getAllSchedulesList();

    Optional<DoctorScheduleDTO> getScheduleById(UUID id);

    DoctorScheduleDTO createSchedule(DoctorScheduleDTO doctorScheduleDTO);

    DoctorScheduleDTO editScheduleTime(UUID scheduleId, LocalTime startWorkingHour, ScheduleTimeDTO scheduleTimeDTO);

    void deleteSchedule(UUID id);

    void deleteScheduleTime(UUID scheduleId, LocalTime startWorkingHour);
}
