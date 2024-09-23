package com.final_project_clinic.doctor.mapper;

import com.final_project_clinic.doctor.data.model.Doctor;
import com.final_project_clinic.doctor.data.model.DoctorSchedule;
import com.final_project_clinic.doctor.data.model.ScheduleTime;
import com.final_project_clinic.doctor.dto.DoctorScheduleDTO;
import com.final_project_clinic.doctor.dto.ScheduleTimeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DoctorScheduleMapperTest {

    @Autowired
    private DoctorScheduleMapper doctorScheduleMapper;

    @Test
    void toDoctorScheduleDTOTest() {
        UUID doctorId = UUID.randomUUID();
        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .name("Doctor A")
                .specialization("General")
                .identificationNumber("236789")
                .phoneNumber("+6283929273729")
                .gender("Male")
                .build();

        ScheduleTime scheduleTime = ScheduleTime.builder()
                .startWorkingHour(LocalTime.of(9, 0))
                .endWorkingHour(LocalTime.of(17, 0))
                .maxPatient(10)
                .build();

        DoctorSchedule doctorSchedule = DoctorSchedule.builder()
                .id(UUID.randomUUID())
                .doctor(doctor)
                .day("Monday")
                .scheduleTimes(Collections.singletonList(scheduleTime))
                .build();

        DoctorScheduleDTO doctorScheduleDTO = doctorScheduleMapper.toDoctorScheduleDTO(doctorSchedule);

        assertNotNull(doctorScheduleDTO);
        assertEquals(doctorSchedule.getDoctor().getId(), doctorScheduleDTO.getDoctorId());
        assertEquals(doctorSchedule.getDay(), doctorScheduleDTO.getDay());
        assertEquals(doctorSchedule.getScheduleTimes().size(), doctorScheduleDTO.getScheduleTimes().size());
    }

    @Test
    void toDoctorScheduleTest() {
        UUID doctorId = UUID.randomUUID();
        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .name("Doctor A")
                .specialization("General")
                .identificationNumber("236789")
                .phoneNumber("+6283929273729")
                .gender("Male")
                .build();

        ScheduleTimeDTO scheduleTimeDTO = new ScheduleTimeDTO(
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                10,
                null,
                null,
                null,
                null
        );

        DoctorScheduleDTO doctorScheduleDTO = new DoctorScheduleDTO(
                UUID.randomUUID(),
                doctorId,
                "Monday",
                Collections.singletonList(scheduleTimeDTO)
        );

        DoctorSchedule doctorSchedule = doctorScheduleMapper.toDoctorSchedule(doctorScheduleDTO, doctor);

        assertNotNull(doctorSchedule);
        assertEquals(doctor.getId(), doctorSchedule.getDoctor().getId());
        assertEquals(doctorScheduleDTO.getDay(), doctorSchedule.getDay());
        assertEquals(doctorScheduleDTO.getScheduleTimes().size(), doctorSchedule.getScheduleTimes().size());
    }
}
