package com.final_project_clinic.user.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import com.final_project_clinic.user.data.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class PatientShowDTOTest {

    private final ObjectMapper objectMapper;

    public PatientShowDTOTest() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    void testPatientShowDTOConstructor() {
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

        PatientShowDTO patientShowDTO = new PatientShowDTO(
                id, user, nik, phoneNumber, address, gender,
                dateOfBirth, createdTime, updatedTime, createdBy, updatedBy
        );

        assertAll("PatientShowDTO",
                () -> assertEquals(id, patientShowDTO.getId()),
                () -> assertEquals(user, patientShowDTO.getUser()),
                () -> assertEquals(nik, patientShowDTO.getNik()),
                () -> assertEquals(phoneNumber, patientShowDTO.getPhoneNumber()),
                () -> assertEquals(address, patientShowDTO.getAddress()),
                () -> assertEquals(gender, patientShowDTO.getGender()),
                () -> assertEquals(dateOfBirth, patientShowDTO.getDateOfBirth()),
                () -> assertEquals(createdTime, patientShowDTO.getCreatedTime()),
                () -> assertEquals(updatedTime, patientShowDTO.getUpdatedTime()),
                () -> assertEquals(createdBy, patientShowDTO.getCreatedBy()),
                () -> assertEquals(updatedBy, patientShowDTO.getUpdatedBy())
        );
    }

    @Test
    void testSetters() {
        PatientShowDTO patientShowDTO = new PatientShowDTO();

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

        patientShowDTO.setId(id);
        patientShowDTO.setUser(user);
        patientShowDTO.setNik(nik);
        patientShowDTO.setPhoneNumber(phoneNumber);
        patientShowDTO.setAddress(address);
        patientShowDTO.setGender(gender);
        patientShowDTO.setDateOfBirth(dateOfBirth);
        patientShowDTO.setCreatedTime(createdTime);
        patientShowDTO.setUpdatedTime(updatedTime);
        patientShowDTO.setCreatedBy(createdBy);
        patientShowDTO.setUpdatedBy(updatedBy);

        assertAll("PatientShowDTO Setters",
                () -> assertEquals(id, patientShowDTO.getId()),
                () -> assertEquals(user, patientShowDTO.getUser()),
                () -> assertEquals(nik, patientShowDTO.getNik()),
                () -> assertEquals(phoneNumber, patientShowDTO.getPhoneNumber()),
                () -> assertEquals(address, patientShowDTO.getAddress()),
                () -> assertEquals(gender, patientShowDTO.getGender()),
                () -> assertEquals(dateOfBirth, patientShowDTO.getDateOfBirth()),
                () -> assertEquals(createdTime, patientShowDTO.getCreatedTime()),
                () -> assertEquals(updatedTime, patientShowDTO.getUpdatedTime()),
                () -> assertEquals(createdBy, patientShowDTO.getCreatedBy()),
                () -> assertEquals(updatedBy, patientShowDTO.getUpdatedBy())
        );
    }

    @Test
    void testJsonSerialization() throws Exception {
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

        PatientShowDTO patientShowDTO = new PatientShowDTO(
                id, user, nik, phoneNumber, address, gender,
                dateOfBirth, createdTime, updatedTime, createdBy, updatedBy
        );

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(patientShowDTO);
        assertNotNull(json);
        assertTrue(json.contains("\"id\":\"" + id.toString() + "\""));
        assertTrue(json.contains("\"nik\":" + nik));
        assertTrue(json.contains("\"phoneNumber\":\"" + phoneNumber + "\""));
        assertTrue(json.contains("\"address\":\"" + address + "\""));
        assertTrue(json.contains("\"gender\":\"" + gender + "\""));
        assertTrue(json.contains("\"dateOfBirth\":\"" + dateOfBirth + "\""));
        assertFalse(json.contains("\"createdTime\":\"" + createdTime.toString() + "\""));
        assertFalse(json.contains("\"updatedTime\":\"" + updatedTime.toString() + "\""));
        assertTrue(json.contains("\"createdBy\":\"" + createdBy + "\""));
        assertTrue(json.contains("\"updatedBy\":\"" + updatedBy + "\""));
    }

    @Test
    void testJsonDeserialization() throws Exception {
        String json = "{\"id\":\"b6f5d3f2-391c-4d3d-8a59-60c71fdab700\",\"nik\":123456789,\"phoneNumber\":\"123-456-7890\",\"address\":\"123 Main St\",\"gender\":\"Male\",\"dateOfBirth\":\"1990-01-01\",\"createdTime\":\"2024-09-19T10:15:30\",\"updatedTime\":\"2024-09-19T10:15:30\",\"createdBy\":\"admin\",\"updatedBy\":\"admin\"}";

        PatientShowDTO patientShowDTO = objectMapper.readValue(json, PatientShowDTO.class);

        assertNotNull(patientShowDTO);
        assertEquals(UUID.fromString("b6f5d3f2-391c-4d3d-8a59-60c71fdab700"), patientShowDTO.getId());
        assertEquals(123456789L, patientShowDTO.getNik());
        assertEquals("123-456-7890", patientShowDTO.getPhoneNumber());
        assertEquals("123 Main St", patientShowDTO.getAddress());
        assertEquals("Male", patientShowDTO.getGender());
        assertEquals(LocalDate.of(1990, 1, 1), patientShowDTO.getDateOfBirth());
        assertEquals(LocalDateTime.of(2024, 9, 19, 10, 15, 30), patientShowDTO.getCreatedTime());
        assertEquals(LocalDateTime.of(2024, 9, 19, 10, 15, 30), patientShowDTO.getUpdatedTime());
        assertEquals("admin", patientShowDTO.getCreatedBy());
        assertEquals("admin", patientShowDTO.getUpdatedBy());
    }
}
