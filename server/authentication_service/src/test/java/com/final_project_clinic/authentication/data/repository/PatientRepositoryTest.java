//package com.final_project_clinic.authentication.data.repository;
//
//import com.final_project_clinic.authentication.data.model.Patient;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.dao.EmptyResultDataAccessException;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@DataJpaTest
//class PatientRepositoryTest {
//
//    @Autowired
//    private PatientRepository patientRepository;
//
//    private Patient testPatient;
//
//    @BeforeEach
//    void setUp() {
//        // Create and save a test patient
//        testPatient = new Patient();
//        testPatient.setId(UUID.randomUUID());
//        testPatient.setNik(3174123412341234L);
//        // Set other fields as necessary
//        testPatient.setGender("Male"); // Example field
//        patientRepository.save(testPatient);
//    }
//
//    @Test
//    void whenFindByNik_thenReturnPatient() {
//        // When
//        Patient foundPatient = patientRepository.findPatientByNik(123456L);
//
//        // Then
//        assertThat(foundPatient).isNotNull();
//        assertThat(foundPatient.getNik()).isEqualTo(123456L);
//        assertThat(foundPatient.getId()).isEqualTo(testPatient.getId());
//    }
//
//    @Test
//    void whenFindByNikNotExists_thenReturnNull() {
//        // When
//        Patient foundPatient = patientRepository.findPatientByNik(3174123412341234L);
//
//        // Then
//        assertThat(foundPatient).isNull();
//    }
//
//    @Test
//    void whenSavePatient_thenFindPatientById() {
//        // Given
//        Patient patient = new Patient();
//        patient.setId(UUID.randomUUID());
//        patient.setNik(3174123412341234L);
//        patient.setGender("Female"); // Example field
//        patientRepository.save(patient);
//
//        // When
//        Optional<Patient> foundPatient = patientRepository.findById(patient.getId());
//
//        // Then
//        assertThat(foundPatient).isPresent();
//        assertThat(foundPatient.get().getNik()).isEqualTo(654321L);
//    }
//
//    @Test
//    void whenDeletePatient_thenPatientShouldNotExist() {
//        // Given
//        Patient patientToDelete = new Patient();
//        patientToDelete.setId(UUID.randomUUID());
//        patientToDelete.setNik(3174123412341234L);
//        patientRepository.save(patientToDelete);
//
//        // When
//        patientRepository.delete(patientToDelete);
//
//        // Then
//        Optional<Patient> foundPatient = patientRepository.findById(patientToDelete.getId());
//        assertThat(foundPatient).isNotPresent();
//    }
//}
