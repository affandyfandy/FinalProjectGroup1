package com.final_project_clinic.user.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.ConstraintViolation;

class PatientSaveDTOTest {

    private final Validator validator;

    public PatientSaveDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidPatientSaveDTO() {
        UUID userId = UUID.randomUUID();
        Long nik = 123456789L;
        String phoneNumber = "1234567890"; // Valid phone number with 10 digits
        String address = "123 Main St";
        String gender = "Male";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);

        PatientSaveDTO patientSaveDTO = new PatientSaveDTO(
                userId, nik, phoneNumber, address, gender, dateOfBirth
        );

        // Validate and assert no violations
        var violations = validator.validate(patientSaveDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidNIK() {
        PatientSaveDTO patientSaveDTO = new PatientSaveDTO(
                UUID.randomUUID(), null, "1234567890", "123 Main St", "Male", LocalDate.of(1990, 1, 1)
        );

        var violations = validator.validate(patientSaveDTO);
        assertFalse(violations.isEmpty());

        // Check that the violation is for the NIK field
        for (ConstraintViolation<PatientSaveDTO> violation : violations) {
            if (violation.getPropertyPath().toString().equals("nik")) {
                assertEquals("NIK cannot be null", violation.getMessage());
            }
        }
    }

    @Test
    void testInvalidPhoneNumber() {
        PatientSaveDTO patientSaveDTO = new PatientSaveDTO(
                UUID.randomUUID(), 123456789L, "123", "123 Main St", "Male", LocalDate.of(1990, 1, 1)
        );

        var violations = validator.validate(patientSaveDTO);
        assertFalse(violations.isEmpty());

        // Check that the violation is for the phoneNumber field
        for (ConstraintViolation<PatientSaveDTO> violation : violations) {
            if (violation.getPropertyPath().toString().equals("phoneNumber")) {
                assertEquals("Phone number must be between 10 and 20 digits", violation.getMessage());
            }
        }
    }

    @Test
    void testEmptyPatientSaveDTO() {
        PatientSaveDTO patientSaveDTO = new PatientSaveDTO();

        var violations = validator.validate(patientSaveDTO);
        assertFalse(violations.isEmpty());

        // Check that violations are for the fields with constraints
        for (ConstraintViolation<PatientSaveDTO> violation : violations) {
            if (violation.getPropertyPath().toString().equals("nik")) {
                assertEquals("NIK cannot be null", violation.getMessage());
            }
            if (violation.getPropertyPath().toString().equals("phoneNumber")) {
                assertEquals("Phone number must be between 10 and 20 digits", violation.getMessage());
            }
        }
    }

    @Test
    void testSetters() {
        PatientSaveDTO patientSaveDTO = new PatientSaveDTO();

        UUID userId = UUID.randomUUID();
        Long nik = 987654321L;
        String phoneNumber = "0987654321"; // Valid phone number
        String address = "456 Elm St";
        String gender = "Female";
        LocalDate dateOfBirth = LocalDate.of(1995, 5, 15);

        patientSaveDTO.setUserId(userId);
        patientSaveDTO.setNik(nik);
        patientSaveDTO.setPhoneNumber(phoneNumber);
        patientSaveDTO.setAddress(address);
        patientSaveDTO.setGender(gender);
        patientSaveDTO.setDateOfBirth(dateOfBirth);

        assertAll("PatientSaveDTO Setters",
                () -> assertEquals(userId, patientSaveDTO.getUserId()),
                () -> assertEquals(nik, patientSaveDTO.getNik()),
                () -> assertEquals(phoneNumber, patientSaveDTO.getPhoneNumber()),
                () -> assertEquals(address, patientSaveDTO.getAddress()),
                () -> assertEquals(gender, patientSaveDTO.getGender()),
                () -> assertEquals(dateOfBirth, patientSaveDTO.getDateOfBirth())
        );
    }
}
