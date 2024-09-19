package com.final_project_clinic.user.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import com.final_project_clinic.user.data.model.User;

class PatientDTOTest {

    @Test
    void testPatientDTOConstructor() {
        UUID id = UUID.randomUUID();
        User user = new User();  // Assuming User class has a no-args constructor
        Long nik = 123456789L;
        String phoneNumber = "123-456-7890";
        String address = "123 Main St";
        String gender = "Male";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now();
        String createdBy = "admin";
        String updatedBy = "admin";

        PatientDTO patientDTO = new PatientDTO(
                id, user, nik, phoneNumber, address, gender,
                dateOfBirth, createdTime, updatedTime, createdBy, updatedBy
        );

        assertAll("PatientDTO",
                () -> assertEquals(id, patientDTO.getId()),
                () -> assertEquals(user, patientDTO.getUser()),
                () -> assertEquals(nik, patientDTO.getNik()),
                () -> assertEquals(phoneNumber, patientDTO.getPhoneNumber()),
                () -> assertEquals(address, patientDTO.getAddress()),
                () -> assertEquals(gender, patientDTO.getGender()),
                () -> assertEquals(dateOfBirth, patientDTO.getDateOfBirth()),
                () -> assertEquals(createdTime, patientDTO.getCreatedTime()),
                () -> assertEquals(updatedTime, patientDTO.getUpdatedTime()),
                () -> assertEquals(createdBy, patientDTO.getCreatedBy()),
                () -> assertEquals(updatedBy, patientDTO.getUpdatedBy())
        );
    }

    @Test
    void testSetters() {
        PatientDTO patientDTO = new PatientDTO();

        UUID id = UUID.randomUUID();
        User user = new User();  // Assuming User class has a no-args constructor
        Long nik = 987654321L;
        String phoneNumber = "098-765-4321";
        String address = "456 Elm St";
        String gender = "Female";
        LocalDate dateOfBirth = LocalDate.of(1995, 5, 15);
        LocalDateTime createdTime = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedTime = LocalDateTime.now().minusHours(1);
        String createdBy = "system";
        String updatedBy = "system";

        patientDTO.setId(id);
        patientDTO.setUser(user);
        patientDTO.setNik(nik);
        patientDTO.setPhoneNumber(phoneNumber);
        patientDTO.setAddress(address);
        patientDTO.setGender(gender);
        patientDTO.setDateOfBirth(dateOfBirth);
        patientDTO.setCreatedTime(createdTime);
        patientDTO.setUpdatedTime(updatedTime);
        patientDTO.setCreatedBy(createdBy);
        patientDTO.setUpdatedBy(updatedBy);

        assertAll("PatientDTO Setters",
                () -> assertEquals(id, patientDTO.getId()),
                () -> assertEquals(user, patientDTO.getUser()),
                () -> assertEquals(nik, patientDTO.getNik()),
                () -> assertEquals(phoneNumber, patientDTO.getPhoneNumber()),
                () -> assertEquals(address, patientDTO.getAddress()),
                () -> assertEquals(gender, patientDTO.getGender()),
                () -> assertEquals(dateOfBirth, patientDTO.getDateOfBirth()),
                () -> assertEquals(createdTime, patientDTO.getCreatedTime()),
                () -> assertEquals(updatedTime, patientDTO.getUpdatedTime()),
                () -> assertEquals(createdBy, patientDTO.getCreatedBy()),
                () -> assertEquals(updatedBy, patientDTO.getUpdatedBy())
        );
    }

    @Test
    void testEmptyConstructor() {
        PatientDTO patientDTO = new PatientDTO();

        assertAll("PatientDTO Default Values",
                () -> assertNull(patientDTO.getId()),
                () -> assertNull(patientDTO.getUser()),
                () -> assertNull(patientDTO.getNik()),
                () -> assertNull(patientDTO.getPhoneNumber()),
                () -> assertNull(patientDTO.getAddress()),
                () -> assertNull(patientDTO.getGender()),
                () -> assertNull(patientDTO.getDateOfBirth()),
                () -> assertNull(patientDTO.getCreatedTime()),
                () -> assertNull(patientDTO.getUpdatedTime()),
                () -> assertNull(patientDTO.getCreatedBy()),
                () -> assertNull(patientDTO.getUpdatedBy())
        );
    }
}
