package com.final_project_clinic.user.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testForbiddenAccess() throws Exception {
        mockMvc.perform(get("/api/v1/all/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/api/v1/all/users/public"))
                .andExpect(status().isForbidden());
    }
}