package com.final_project_clinic.doctor.service.impl;

import com.final_project_clinic.doctor.data.model.Doctor;
import com.final_project_clinic.doctor.data.model.DoctorSchedule;
import com.final_project_clinic.doctor.data.model.ScheduleTime;
import com.final_project_clinic.doctor.data.repository.DoctorRepository;
import com.final_project_clinic.doctor.data.repository.DoctorScheduleRepository;
import com.final_project_clinic.doctor.data.repository.ScheduleTimeRepository;
import com.final_project_clinic.doctor.dto.DoctorScheduleDTO;
import com.final_project_clinic.doctor.dto.DoctorScheduleShowDTO;
import com.final_project_clinic.doctor.dto.ScheduleTimeDTO;
import com.final_project_clinic.doctor.mapper.DoctorScheduleMapper;
import com.final_project_clinic.doctor.mapper.ScheduleTimeMapper;
import com.final_project_clinic.doctor.service.DoctorScheduleService;
import com.final_project_clinic.doctor.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final ScheduleTimeRepository scheduleTimeRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleMapper doctorScheduleMapper;
    private final ScheduleTimeMapper scheduleTimeMapper;

    @Autowired
    public DoctorScheduleServiceImpl(DoctorScheduleRepository doctorScheduleRepository,
            ScheduleTimeRepository scheduleTimeRepository,
            DoctorRepository doctorRepository,
            DoctorScheduleMapper doctorScheduleMapper,
            ScheduleTimeMapper scheduleTimeMapper) {
        this.doctorScheduleRepository = doctorScheduleRepository;
        this.scheduleTimeRepository = scheduleTimeRepository;
        this.doctorRepository = doctorRepository;
        this.doctorScheduleMapper = doctorScheduleMapper;
        this.scheduleTimeMapper = scheduleTimeMapper;
    }

    @Override
    public Page<DoctorScheduleDTO> getAllSchedules(Pageable pageable) {
        Page<DoctorSchedule> schedules = doctorScheduleRepository.findAll(pageable);
        return schedules.map(doctorScheduleMapper::toDoctorScheduleDTO);
    }

    @Override
    public List<DoctorScheduleDTO> getAllSchedulesList() {
        List<DoctorSchedule> schedules = doctorScheduleRepository.findAll();
        return schedules.stream()
                .map(doctorScheduleMapper::toDoctorScheduleDTO)
                .toList();
    }

    @Override
    public List<DoctorScheduleShowDTO> getAllSchedulesDoctor() {
        List<DoctorSchedule> schedules = doctorScheduleRepository.findAll();
        return schedules.stream() // Use stream() to convert the List to a Stream
                .map(doctorScheduleMapper::toDoctorScheduleDTOShow) // Apply the map function
                .toList(); // Collect the result back into a List
    }

    @Override
    public Optional<DoctorScheduleDTO> getScheduleById(UUID id) {
        return doctorScheduleRepository.findById(id).map(doctorScheduleMapper::toDoctorScheduleDTO);
    }

    @Override
    public Optional<ScheduleTimeDTO> getScheduleTime(UUID scheduleId, LocalTime startWorkingHour) {
        Optional<DoctorSchedule> doctorScheduleOpt = doctorScheduleRepository.findById(scheduleId);
        if (doctorScheduleOpt.isEmpty()) {
            return Optional.empty();
        }

        DoctorSchedule doctorSchedule = doctorScheduleOpt.get();

        Optional<ScheduleTime> scheduleTimeOpt = doctorSchedule.getScheduleTimes().stream()
                .filter(st -> st.getStartWorkingHour().equals(startWorkingHour))
                .findFirst();

        return scheduleTimeOpt.map(scheduleTimeMapper::toScheduleTimeDTO);
    }



    @Override
    public DoctorScheduleDTO createSchedule(DoctorScheduleDTO doctorScheduleDTO) {
        Doctor doctor = doctorRepository.findById(doctorScheduleDTO.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        Optional<DoctorSchedule> existingScheduleOpt = doctorScheduleRepository
                .findByDoctorIdAndDay(doctor.getId(), doctorScheduleDTO.getDay());

        DoctorSchedule doctorSchedules;

        if (existingScheduleOpt.isPresent()) {
            doctorSchedules = existingScheduleOpt.get();
        } else {
            doctorSchedules = DoctorSchedule.builder()
                    .doctor(doctor)
                    .day(doctorScheduleDTO.getDay())
                    .build();
            doctorSchedules = doctorScheduleRepository.save(doctorSchedules);

            doctorSchedules.setScheduleTimes(new ArrayList<>());
        }

        ValidationUtils.validateNoInternalOverlap(doctorScheduleDTO.getScheduleTimes());

        ValidationUtils.validateNoOverlap(doctorSchedules, doctorScheduleDTO.getScheduleTimes());

        DoctorSchedule finalDoctorSchedules = doctorSchedules;
        List<ScheduleTime> savedScheduleTimes = doctorScheduleDTO.getScheduleTimes().stream().map(timeDTO -> {
            ScheduleTime scheduleTime = ScheduleTime.builder()
                    .doctorSchedule(finalDoctorSchedules)
                    .startWorkingHour(timeDTO.getStartWorkingHour())
                    .endWorkingHour(timeDTO.getEndWorkingHour())
                    .maxPatient(timeDTO.getMaxPatient())
                    .build();
            return scheduleTimeRepository.save(scheduleTime);
        }).toList();

        finalDoctorSchedules.setScheduleTimes(savedScheduleTimes);

        return doctorScheduleMapper.toDoctorScheduleDTO(doctorSchedules);
    }

    public DoctorScheduleDTO editScheduleTime(UUID scheduleId, LocalTime startWorkingHour,
            ScheduleTimeDTO scheduleTimeDTO) {
        Optional<DoctorSchedule> doctorScheduleOpt = doctorScheduleRepository.findById(scheduleId);
        if (doctorScheduleOpt.isEmpty()) {
            throw new IllegalArgumentException("Schedule not found");
        }

        DoctorSchedule doctorSchedule = doctorScheduleOpt.get();

        ScheduleTime existingScheduleTime = doctorSchedule.getScheduleTimes().stream()
                .filter(st -> st.getStartWorkingHour().equals(startWorkingHour))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Schedule time not found"));

        List<ScheduleTime> otherScheduleTimes = doctorSchedule.getScheduleTimes().stream()
                .filter(st -> !st.getStartWorkingHour().equals(startWorkingHour))
                .toList();

        ValidationUtils.validateNoOverlapEdit(otherScheduleTimes, scheduleTimeDTO);

        existingScheduleTime.setEndWorkingHour(scheduleTimeDTO.getEndWorkingHour());
        existingScheduleTime.setMaxPatient(scheduleTimeDTO.getMaxPatient());

        scheduleTimeRepository.save(existingScheduleTime);

        return doctorScheduleMapper.toDoctorScheduleDTO(doctorSchedule);
    }

    @Override
    public void deleteSchedule(UUID id) {
        doctorScheduleRepository.deleteById(id);
    }

    @Override
    public void deleteScheduleTime(UUID scheduleId, LocalTime startWorkingHour) {
        Optional<DoctorSchedule> doctorScheduleOpt = doctorScheduleRepository.findById(scheduleId);
        if (doctorScheduleOpt.isEmpty()) {
            throw new IllegalArgumentException("Schedule not found");
        }

        DoctorSchedule doctorSchedule = doctorScheduleOpt.get();

        ScheduleTime scheduleTimeToDelete = doctorSchedule.getScheduleTimes().stream()
                .filter(st -> st.getStartWorkingHour().equals(startWorkingHour))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Schedule time not found"));

        doctorSchedule.getScheduleTimes().remove(scheduleTimeToDelete);

        doctorScheduleRepository.save(doctorSchedule);

        scheduleTimeRepository.delete(scheduleTimeToDelete);
    }

    @Override
    public Optional<DoctorScheduleDTO> getDoctorScheduleByDay(UUID doctorId, String day) {
        Optional<DoctorSchedule> doctorSchedule = doctorScheduleRepository.findByDoctorIdAndDay(doctorId, day);
        return doctorSchedule.map(doctorScheduleMapper::toDoctorScheduleDTO);
    }
}
