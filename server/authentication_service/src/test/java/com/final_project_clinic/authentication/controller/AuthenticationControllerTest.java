package com.final_project_clinic.authentication.controller;

import com.final_project_clinic.authentication.dto.LoginRequestDTO;
import com.final_project_clinic.authentication.dto.ProfileResponseDTO;
import com.final_project_clinic.authentication.dto.RegisterRequestDTO;
import com.final_project_clinic.authentication.dto.RegisterResponseDTO;
import com.final_project_clinic.authentication.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final String validToken = "eyJhbGciOiJSUzI1NiJ9.eyJyb2xlIjoiU1VQRVJBRE1JTiIsImlkIjoiMWM0NmExZGQtNDFlMS00Nzc1LWJkY2YtNmQ0YzZkODk4Nzc5IiwiZW1haWwiOiJlbWFpbDFkMjEzMjFAZ21haWwuY29tIiwic3ViIjoiZW1haWwxZDIxMzIxQGdtYWlsLmNvbSIsImlhdCI6MTcyNjcwNjAwOSwiZXhwIjoxNzI2NzQyMDA5fQ.TPcqK7vlBW0kJneym-opC9im-Cfr2JS_jDKAzRmHEkrET5_uIKVIELUT0k5CFDmaFcRazSIaLkY4f2wxzBAayRF1wtaaUa8edsOHfbAqlhWIBqKnvug2Y2Brg25YXIGelPgt6dZ22VGuqqnrQD1P2h7USIcALyLCSddHKHcNOchF7Ufy1qiv3Zxf0s8Qm6lELYZvZ39d5bCJlAKcXcotSGukKNo_AGfokiqtjQRXjfHkYG-v6-HkxpzhNzWa4q1PUApSIl89mZRLrTjbYq0f64TGPMlacWNZQyVQGmPn0hdAnO_QUejtL-24tukB8IFUFjF8Sqbe-kLVFfGj7Ahqvw";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void loginUser_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
        // Invalid login request with missing username or password
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("");
        loginRequest.setPassword("password");

        String requestJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/v1/authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_shouldReturnSuccessMessage_whenValidRequest() throws Exception {
        // Valid register request with all fields correctly formatted
        RegisterResponseDTO registerResponse = new RegisterResponseDTO();
        registerResponse.setMessage("User registered successfully");

        when(authService.register(any(RegisterRequestDTO.class)))
                .thenReturn(registerResponse);

        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setNik(3174123412341234L);
        registerRequest.setRole("ADMIN");
        registerRequest.setFullName("newUser");
        registerRequest.setPassword("Password123!");
        registerRequest.setEmail("email@gmail.com");

        String requestJson = objectMapper.writeValueAsString(registerRequest);

        mockMvc.perform(post("/api/v1/authentication/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void registerUser_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
        // Invalid register request with missing email or improperly formatted email
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setFullName("newUser");
        registerRequest.setPassword("Password123!");
        registerRequest.setEmail("invalid-email");  // Invalid email format

        String requestJson = objectMapper.writeValueAsString(registerRequest);

        mockMvc.perform(post("/api/v1/authentication/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProfile_shouldReturnProfileData_whenValidToken() throws Exception {
        ProfileResponseDTO profileResponse = new ProfileResponseDTO();
        profileResponse.setFullName("newUser");
        profileResponse.setEmail("user@gmail.com");

        when(authService.getProfile("mock-jwt-token"))
                .thenReturn(profileResponse);

        mockMvc.perform(get("/api/v1/authentication/profile")
                        .header("Authorization", "Bearer mock-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("newUser"))
                .andExpect(jsonPath("$.email").value("user@gmail.com"));
    }

}
