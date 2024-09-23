package com.final_project_clinic.doctor.mapper;

import com.final_project_clinic.doctor.data.model.DoctorSchedule;
import com.final_project_clinic.doctor.data.model.ScheduleTime;
import com.final_project_clinic.doctor.dto.ScheduleTimeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ScheduleTimeMapperTest {

    @Autowired
    private ScheduleTimeMapper scheduleTimeMapper;

    @Test
    void toScheduleTimeDTOTest() {
        DoctorSchedule doctorSchedule = DoctorSchedule.builder()
                .id(UUID.randomUUID())
                .day("Monday")
                .build();

        ScheduleTime scheduleTime = ScheduleTime.builder()
                .doctorSchedule(doctorSchedule)
                .startWorkingHour(LocalTime.of(9, 0))
                .endWorkingHour(LocalTime.of(17, 0))
                .maxPatient(10)
                .build();

        ScheduleTimeDTO scheduleTimeDTO = scheduleTimeMapper.toScheduleTimeDTO(scheduleTime);

        assertNotNull(scheduleTimeDTO);
        assertEquals(scheduleTime.getStartWorkingHour(), scheduleTimeDTO.getStartWorkingHour());
        assertEquals(scheduleTime.getEndWorkingHour(), scheduleTimeDTO.getEndWorkingHour());
        assertEquals(scheduleTime.getMaxPatient(), scheduleTimeDTO.getMaxPatient());
    }

    @Test
    void toScheduleTimeEntityTest() {
        DoctorSchedule doctorSchedule = DoctorSchedule.builder()
                .id(UUID.randomUUID())
                .day("Monday")
                .build();

        ScheduleTimeDTO scheduleTimeDTO = new ScheduleTimeDTO(
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                10,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "admin",
                "admin"
        );

        ScheduleTime scheduleTime = scheduleTimeMapper.toScheduleTime(scheduleTimeDTO, doctorSchedule);

        assertNotNull(scheduleTime);
        assertEquals(scheduleTimeDTO.getStartWorkingHour(), scheduleTime.getStartWorkingHour());
        assertEquals(scheduleTimeDTO.getEndWorkingHour(), scheduleTime.getEndWorkingHour());
        assertEquals(scheduleTimeDTO.getMaxPatient(), scheduleTime.getMaxPatient());
    }
}
