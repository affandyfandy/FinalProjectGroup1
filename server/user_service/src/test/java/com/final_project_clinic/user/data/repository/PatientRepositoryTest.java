
// package com.final_project_clinic.user.data.repository;

// import static org.assertj.core.api.Assertions.assertThat;

// import java.util.UUID;

// import com.final_project_clinic.user.data.model.Patient;
// import com.final_project_clinic.user.data.model.User;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// @DataJpaTest
// class PatientRepositoryTest {

//    @Autowired
//    private PatientRepository patientRepository;

//    @Autowired
//    private UserRepository userRepository;

//    private User dummyUser;
//    private Patient dummyPatient;

//    @BeforeEach
//    void setUp() {
//        // Create a dummy User
//        dummyUser = new User();
//        dummyUser.setId(UUID.randomUUID());
//        dummyUser.setFullName("John Doe");
//        dummyUser.setEmail("johndoe@gmail.com");
//        dummyUser.setPassword("Password123!");
//        dummyUser.setRole("ADMIN");

//        // Save the user in the database
//        userRepository.save(dummyUser);

//        // Create a dummy Patient
//        dummyPatient = new Patient();
//        dummyPatient.setId(UUID.randomUUID());
//        dummyPatient.setUser(dummyUser);
//        dummyPatient.setNik(3174123412341234L);
//        dummyPatient.setPhoneNumber("08123456789");
//        dummyPatient.setAddress("Some address");
//        dummyPatient.setGender("Male");

//        // Save the patient in the database
//        patientRepository.save(dummyPatient);
//    }

//    @Test
//    void testFindByUser() {
//        // Find the patient by user
//        Patient foundPatient = patientRepository.findByUser(dummyUser);
//        assertThat(foundPatient).isNotNull();
//        assertThat(foundPatient.getUser()).isEqualTo(dummyUser);
//    }

//    @Test
//    void testFindPatientByNik() {
//        // Find the patient by NIK
//        Patient foundPatient = patientRepository.findPatientByNik(dummyPatient.getNik());
//        assertThat(foundPatient).isNotNull();
//        assertThat(foundPatient.getNik()).isEqualTo(dummyPatient.getNik());
//    }

//    @Test
//    void testFindPatientByPhoneNumber() {
//        // Find the patient by phone number
//        Patient foundPatient = patientRepository.findPatientByPhoneNumber(dummyPatient.getPhoneNumber());
//        assertThat(foundPatient).isNotNull();
//        assertThat(foundPatient.getPhoneNumber()).isEqualTo(dummyPatient.getPhoneNumber());
//    }

//    @Test
//    void testPatientNotFoundByNik() {
//        // Search for a non-existent patient by NIK
//        Patient foundPatient = patientRepository.findPatientByNik(999999999999L);
//        assertThat(foundPatient).isNull();
//    }

//    @Test
//    void testPatientNotFoundByPhoneNumber() {
//        // Search for a non-existent patient by phone number
//        Patient foundPatient = patientRepository.findPatientByPhoneNumber("081234567890");
//        assertThat(foundPatient).isNull();
//    }
// }

