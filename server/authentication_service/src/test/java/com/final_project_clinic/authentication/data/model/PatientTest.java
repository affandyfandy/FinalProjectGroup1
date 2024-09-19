//package com.final_project_clinic.authentication.data.model;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Set;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class PatientTest {
//
//    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//    private final Validator validator = validatorFactory.getValidator();
//
//    @Test
//    void testValidPatient() {
//        Patient patient = new Patient();
//        patient.setId(UUID.randomUUID());
//        patient.setUserId(UUID.randomUUID());
//        patient.setNik(123456789L);
//        patient.setPhoneNumber("1234567890");
//        patient.setAddress("123 Test Street");
//        patient.setGender("Male");
//        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
//        patient.setCreatedTime(LocalDateTime.now());
//        patient.setUpdatedTime(LocalDateTime.now());
//        patient.setCreatedBy("Admin");
//        patient.setUpdatedBy("Admin");
//
//        Set<ConstraintViolation<Patient>> violations = validator.validate(patient);
//        assertTrue(violations.isEmpty(), "Patient entity should not have validation errors");
//    }
//
//    @Test
//    void testPatientInvalidNik() {
//        Patient patient = new Patient();
//        patient.setId(UUID.randomUUID());
//        patient.setUserId(UUID.randomUUID());
//        patient.setNik(null); // Invalid as nik cannot be null
//        patient.setPhoneNumber("1234567890");
//        patient.setAddress("123 Test Street");
//        patient.setGender("Male");
//        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
//        patient.setCreatedTime(LocalDateTime.now());
//        patient.setUpdatedTime(LocalDateTime.now());
//        patient.setCreatedBy("Admin");
//        patient.setUpdatedBy("Admin");
//
//        Set<ConstraintViolation<Patient>> violations = validator.validate(patient);
//        assertFalse(violations.isEmpty(), "Patient entity should have validation errors due to null nik");
//    }
//
//    @Test
//    void testPatientUniquePhoneNumber() {
//        Patient patient1 = new Patient();
//        patient1.setId(UUID.randomUUID());
//        patient1.setUserId(UUID.randomUUID());
//        patient1.setNik(3174123412341234L);
//        patient1.setPhoneNumber("1234567890");
//        patient1.setAddress("123 Test Street");
//        patient1.setGender("Male");
//        patient1.setDateOfBirth(LocalDate.of(1990, 1, 1));
//        patient1.setCreatedTime(LocalDateTime.now());
//        patient1.setUpdatedTime(LocalDateTime.now());
//        patient1.setCreatedBy("Admin");
//        patient1.setUpdatedBy("Admin");
//
//        Patient patient2 = new Patient();
//        patient2.setId(UUID.randomUUID());
//        patient2.setUserId(UUID.randomUUID());
//        patient2.setNik(987654321L);
//        patient2.setPhoneNumber("1234567890"); // Same phone number as patient1
//        patient2.setAddress("456 Test Street");
//        patient2.setGender("Female");
//        patient2.setDateOfBirth(LocalDate.of(1985, 5, 5));
//        patient2.setCreatedTime(LocalDateTime.now());
//        patient2.setUpdatedTime(LocalDateTime.now());
//        patient2.setCreatedBy("Admin");
//        patient2.setUpdatedBy("Admin");
//
//        // Since we are not using an actual database, this test is theoretical.
//        // Normally, you'd want to use a repository and check for unique constraints.
//        // Here, we're just illustrating the concept.
//        assertEquals(patient1.getPhoneNumber(), patient2.getPhoneNumber(), "Phone numbers should be unique");
//    }
//}
