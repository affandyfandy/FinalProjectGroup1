package com.final_project_clinic.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_project_clinic.user.data.model.User;
import com.final_project_clinic.user.dto.PatientDTO;
import com.final_project_clinic.user.dto.PatientSaveDTO;
import com.final_project_clinic.user.dto.PatientShowDTO;
import com.final_project_clinic.user.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PatientControllerTest {

    @InjectMocks
    private PatientController patientController;

    @Mock
    private PatientService patientService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
    }

    @Test
    void shouldGetAllPatients_withPatients() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        PatientShowDTO patientShowDTO = new PatientShowDTO(UUID.randomUUID(), null, 123456789012L, "+62123456789", "Some address", "Male", null, null, null, null, null);
        Page<PatientShowDTO> patientPage = new PageImpl<>(List.of(patientShowDTO), pageable, 1);

        when(patientService.findAllPatients(any(Pageable.class)))
                .thenReturn(patientPage);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(patientPage);

        mockMvc.perform(get("/api/v1/patients")
                        .param("page", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldGetAllPatients_withNoPatients() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        Page<PatientShowDTO> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(patientService.findAllPatients(any(Pageable.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/patients")
                        .param("page", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetPatientById_withValidId() throws Exception {
        UUID patientId = UUID.randomUUID();
        PatientShowDTO patientShowDTO = new PatientShowDTO(patientId, null, 123456789012L, "+62123456789", "Some address", "Male", null, null, null, null, null);

        when(patientService.findPatientById(patientId)).thenReturn(patientShowDTO);

        mockMvc.perform(get("/api/v1/patients/{id}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + patientId + "\",\"nik\":123456789012,\"phoneNumber\":\"+62123456789\",\"address\":\"Some address\",\"gender\":\"Male\"}"));
    }

    @Test
    void shouldCreatePatient_withValidFormat() throws Exception {
        User dummyUser = new User(
                UUID.randomUUID(),               // Generate a random UUID
                "John Doe",                      // Dummy full name
                "johndoe@example.com",           // Dummy email
                "Password123!",                  // Dummy password (must meet your password pattern requirements)
                "ROLE_USER"                      // Dummy role (e.g., ROLE_USER)
        );

        PatientSaveDTO patientSaveDTO = new PatientSaveDTO(
                UUID.randomUUID(),
                123456789012L,                   // NIK should be a long (L at the end)
                "082123456789",
                "Some address",
                "Male",
                null
        );

        // Now fill in all the parameters for the PatientDTO constructor
        PatientDTO patientDTO = new PatientDTO(
                UUID.randomUUID(),dummyUser,3174123412341234L,"089123452123","kkk street","Male", LocalDate.of(1990, 1, 1),  LocalDateTime.now(), LocalDateTime.now().plusDays(1),"Admin@gmail.com","Admin@gmail.com" );


        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(patientSaveDTO);

        when(patientService.createPatient(any(PatientSaveDTO.class))).thenReturn(patientDTO);

        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{"
                        + "\"id\":\"" + patientDTO.getId() + "\","
                        + "\"nik\":" + patientDTO.getNik() + ","
                        + "\"phoneNumber\":\"" + patientDTO.getPhoneNumber() + "\","
                        + "\"address\":\"" + patientDTO.getAddress() + "\","
                        + "\"gender\":\"" + patientDTO.getGender() + "\""
                        + "}"));
    }

    @Test
    void shouldCreatePatient_withInvalidFormat() throws Exception {
        String requestBody = "{ \"nik\": \"invalid\", \"phoneNumber\": \"123456\", \"address\": \"Some address\", \"gender\": \"Male\" }";

        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdatePatient_withValidIdAndFormat() throws Exception {
        User dummyUser = new User(
                UUID.randomUUID(),               // Generate a random UUID
                "John Doe",                      // Dummy full name
                "johndoe@example.com",           // Dummy email
                "Password123!",                  // Dummy password (must meet your password pattern requirements)
                "ROLE_USER"                      // Dummy role (e.g., ROLE_USER)
        );

        PatientSaveDTO patientSaveDTO = new PatientSaveDTO(
                UUID.randomUUID(),
                123456789012L,                   // NIK should be a long (L at the end)
                "082123456789",
                "Some address",
                "Male",
                null
        );

        // Now fill in all the parameters for the PatientDTO constructor
        PatientDTO patientDTO = new PatientDTO(
                UUID.randomUUID(),dummyUser,3174123412341234L,"089123452123","kkk street","Male", LocalDate.of(1990, 1, 1),  LocalDateTime.now(), LocalDateTime.now().plusDays(1),"Admin@gmail.com","Admin@gmail.com" );

        String patientId = UUID.randomUUID().toString();
        // Convert request body to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(patientSaveDTO);

        // Mocking the service call
        when(patientService.updatePatient(any(UUID.class), any(PatientSaveDTO.class))).thenReturn(patientDTO);

        // Perform the PUT request and check response
        mockMvc.perform(put("/api/v1/patients/{id}", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{"
                        + "\"id\":\"" + patientDTO.getId() + "\","
                        + "\"nik\":" + patientDTO.getNik() + ","
                        + "\"phoneNumber\":\"" + patientDTO.getPhoneNumber() + "\","
                        + "\"address\":\"" + patientDTO.getAddress() + "\","
                        + "\"gender\":\"" + patientDTO.getGender() + "\""
                        + "}"));
    }


    @Test
    void shouldDeletePatient_withValidId() throws Exception {
        UUID patientId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/patients/{id}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
