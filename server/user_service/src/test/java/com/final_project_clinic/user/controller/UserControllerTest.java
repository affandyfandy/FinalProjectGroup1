package com.final_project_clinic.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_project_clinic.user.data.model.User;
import com.final_project_clinic.user.dto.UserDTO;
import com.final_project_clinic.user.dto.UserSaveDTO;
import com.final_project_clinic.user.dto.UserShowDTO;
import com.final_project_clinic.user.service.UserService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void shouldGetAllUsers_withUsers() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        UserShowDTO userShowDTO = new UserShowDTO(UUID.randomUUID(), "John Doe", "johndoe@example.com", "ADMIN", null, null, null, null);
        Page<UserShowDTO> userPage = new PageImpl<>(List.of(userShowDTO), pageable, 1);

        when(userService.findAllUsers(any(Pageable.class))).thenReturn(userPage);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(userPage);

        mockMvc.perform(get("/api/v1/all/users")
                        .param("page", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldGetAllUsers_withNoUsers() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        Page<UserShowDTO> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(userService.findAllUsers(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/all/users")
                        .param("page", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetUserById_withValidId() throws Exception {
        UUID userId = UUID.randomUUID();
        UserShowDTO userShowDTO = new UserShowDTO(userId, "John Doe", "johndoe@example.com", "ADMIN", null, null, null, null);

        when(userService.findUserById(userId)).thenReturn(userShowDTO);

        mockMvc.perform(get("/api/v1/all/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + userId + "\",\"fullName\":\"John Doe\",\"email\":\"johndoe@example.com\",\"role\":\"ADMIN\"}"));
    }

    @Test
    void shouldCreateUser_withValidFormat() throws Exception {
        User dummyUser = new User(UUID.randomUUID(), "John Doe", "johndoe@example.com", "Password123!", "ROLE_USER");

        UserSaveDTO userSaveDTO = new UserSaveDTO("John Doe", "johndoe@example.com", "Password123!", "ADMIN");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userSaveDTO);

        when(userService.createUser(any(UserSaveDTO.class))).thenReturn(new UserDTO(dummyUser.getId(),"John Doe","email@gmail.com","ADMIN", LocalDateTime.now(), LocalDateTime.now().plusDays(1),"Admin@gmail.com","Admin@gmail.com"
        ));

        mockMvc.perform(post("/api/v1/all/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":\"" + dummyUser.getId() + "\",\"fullName\":\"John Doe\",\"email\":\"email@gmail.com\",\"role\":\"ADMIN\"}"));
    }

    @Test
    void shouldUpdateUser_withValidIdAndFormat() throws Exception {
        UUID userId = UUID.randomUUID();
        UserSaveDTO userSaveDTO = new UserSaveDTO("John Doe", "johndoe@example.com", "Password123!", "ADMIN");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userSaveDTO);

        when(userService.updateUser(any(UUID.class), any(UserSaveDTO.class))).thenReturn(new UserDTO(userId,"John Doe","email@gmail.com","ADMIN", LocalDateTime.now(), LocalDateTime.now().plusDays(1),"Admin@gmail.com","Admin@gmail.com"
        ));

        mockMvc.perform(put("/api/v1/all/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + userId + "\",\"fullName\":\"John Doe\",\"email\":\"email@gmail.com\",\"role\":\"ADMIN\"}"));
    }

    @Test
    void shouldDeleteUser_withValidId() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/all/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
