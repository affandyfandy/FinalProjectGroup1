package com.final_project_clinic.doctor.mapper;

import com.final_project_clinic.doctor.data.model.Doctor;
import com.final_project_clinic.doctor.dto.DoctorDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DoctorMapperTest {

    private final DoctorMapper doctorMapper = DoctorMapper.INSTANCE;

    @Test
    void toDTOTest() {
        UUID id = UUID.randomUUID();
        Doctor doctor = Doctor.builder()
                .id(id)
                .name("Doctor A")
                .specialization("General")
                .identificationNumber("236789")
                .phoneNumber("+6283929273729")
                .gender("Male")
                .dateOfBirth(LocalDate.of(2004, 11, 27))
                .address("SBH")
                .patientTotal(20)
                .build();

        DoctorDTO doctorDTO = doctorMapper.toDTO(doctor);

        assertNotNull(doctorDTO);
        assertEquals(doctor.getId(), doctorDTO.getId());
        assertEquals(doctor.getName(), doctorDTO.getName());
        assertEquals(doctor.getSpecialization(), doctorDTO.getSpecialization());
        assertEquals(doctor.getIdentificationNumber(), doctorDTO.getIdentificationNumber());
        assertEquals(doctor.getPhoneNumber(), doctorDTO.getPhoneNumber());
        assertEquals(doctor.getDateOfBirth(), doctorDTO.getDateOfBirth());
        assertEquals(doctor.getAddress(), doctorDTO.getAddress());
        assertEquals(doctor.getPatientTotal(), doctorDTO.getPatientTotal());

    }

    @Test
    void toEntityTest() {
        UUID id = UUID.randomUUID();
        DoctorDTO doctorDTO = new DoctorDTO(
                id,
                "Doctor A",
                "General",
                "236789",
                "+6283929273729",
                "Male",
                LocalDate.of(2004, 11, 27),
                "SBH",
                20,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "admin@gmail.com",
                "admin@gmail.com"
        );

        Doctor doctor = doctorMapper.toEntity(doctorDTO);

        assertNotNull(doctor);
        assertEquals(doctorDTO.getId(), doctor.getId());
        assertEquals(doctorDTO.getName(), doctor.getName());
        assertEquals(doctorDTO.getSpecialization(), doctor.getSpecialization());
        assertEquals(doctorDTO.getIdentificationNumber(), doctor.getIdentificationNumber());
        assertEquals(doctorDTO.getPhoneNumber(), doctor.getPhoneNumber());
        assertEquals(doctorDTO.getGender(), doctor.getGender());
        assertEquals(doctorDTO.getDateOfBirth(), doctor.getDateOfBirth());
        assertEquals(doctorDTO.getAddress(), doctor.getAddress());
        assertEquals(doctorDTO.getPatientTotal(), doctor.getPatientTotal());
    }
}
