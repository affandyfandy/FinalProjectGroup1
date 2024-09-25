package com.final_project_clinic.doctor.controller;

import com.final_project_clinic.doctor.dto.DoctorScheduleDTO;
import com.final_project_clinic.doctor.dto.ScheduleTimeDTO;
import com.final_project_clinic.doctor.service.DoctorScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/schedules")
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;

    @Autowired
    public DoctorScheduleController(DoctorScheduleService doctorScheduleService) {
        this.doctorScheduleService = doctorScheduleService;
    }

    @GetMapping
    public ResponseEntity<Page<DoctorScheduleDTO>> getAllSchedules(Pageable pageable) {
        Page<DoctorScheduleDTO> schedules = doctorScheduleService.getAllSchedules(pageable);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/list")
    public ResponseEntity<List<DoctorScheduleDTO>> getAllSchedulesList() {
        List<DoctorScheduleDTO> schedules = doctorScheduleService.getAllSchedulesList();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorScheduleDTO> getScheduleById(@PathVariable UUID id) {
        Optional<DoctorScheduleDTO> schedule = doctorScheduleService.getScheduleById(id);
        return schedule.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @GetMapping("/{scheduleId}/{startWorkingHour}")
    public ResponseEntity<ScheduleTimeDTO> getScheduleTime(
            @PathVariable UUID scheduleId,
            @PathVariable String startWorkingHour) {
        LocalTime startHour = LocalTime.parse(startWorkingHour);
        Optional<ScheduleTimeDTO> scheduleTime = doctorScheduleService.getScheduleTime(scheduleId, startHour);
        return scheduleTime.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @PostMapping
    public ResponseEntity<List<DoctorScheduleDTO>> createSchedule(@RequestBody List<DoctorScheduleDTO> doctorScheduleDTOs) {
        List<DoctorScheduleDTO> createdSchedules = new ArrayList<>();
        for (DoctorScheduleDTO doctorScheduleDTO : doctorScheduleDTOs) {
            DoctorScheduleDTO createdSchedule = doctorScheduleService.createSchedule(doctorScheduleDTO);
            createdSchedules.add(createdSchedule);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedules);
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @PutMapping("/{scheduleId}/{startWorkingHour}")
    public ResponseEntity<DoctorScheduleDTO> updateScheduleTime(
            @PathVariable UUID scheduleId,
            @PathVariable String startWorkingHour,
            @RequestBody ScheduleTimeDTO scheduleTimeDTO) {
        LocalTime startHour = LocalTime.parse(startWorkingHour);
        DoctorScheduleDTO updatedSchedule = doctorScheduleService.editScheduleTime(scheduleId, startHour, scheduleTimeDTO);
        return ResponseEntity.ok(updatedSchedule);
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSchedule(@PathVariable UUID id) {
        Optional<DoctorScheduleDTO> schedule = doctorScheduleService.getScheduleById(id);

        if (schedule.isPresent()) {
            doctorScheduleService.deleteSchedule(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
        }
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @DeleteMapping("/{scheduleId}/{startWorkingHour}")
    public ResponseEntity<Void> deleteScheduleTime(
            @PathVariable UUID scheduleId,
            @PathVariable String startWorkingHour) {
        LocalTime startHour = LocalTime.parse(startWorkingHour);
        doctorScheduleService.deleteScheduleTime(scheduleId, startHour);
        return ResponseEntity.noContent().build();
    }
  
    @GetMapping("/doctor/{doctorId}/day/{day}")
    public ResponseEntity<DoctorScheduleDTO> getDoctorScheduleByDay(
            @PathVariable UUID doctorId,
            @PathVariable String day) {
        Optional<DoctorScheduleDTO> schedule = doctorScheduleService.getDoctorScheduleByDay(doctorId, day);
        return schedule.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
