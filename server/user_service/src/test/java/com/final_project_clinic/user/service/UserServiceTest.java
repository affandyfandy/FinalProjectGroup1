package com.final_project_clinic.user.service;

import com.final_project_clinic.user.data.model.User;
import com.final_project_clinic.user.dto.UserDTO;
import com.final_project_clinic.user.dto.UserSaveDTO;
import com.final_project_clinic.user.dto.UserShowDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        UserShowDTO user1 = new UserShowDTO(
                UUID.randomUUID(),
                "User",
                "user1@gmail.com",
                "ADMIN",
                LocalDateTime.of(2002, 10, 3, 0, 0),
                LocalDateTime.of(2002, 10, 3, 0, 0),  // Same for updatedTime or another value
                "creator1",
                "updater1"
        );

        // user2 instantiation with missing values fixed
        UserShowDTO user2 = new UserShowDTO(
                UUID.randomUUID(),
                "User Two",
                "user2@example.com",
                "USER",
                LocalDateTime.now(),  // Current time for createdTime
                LocalDateTime.now(),  // Current time for updatedTime
                "creator2",
                "updater2"
        );

        Page<UserShowDTO> userPage = new PageImpl<>(Arrays.asList(user1, user2));

        when(userService.findAllUsers(pageable)).thenReturn(userPage);

        Page<UserShowDTO> result = userService.findAllUsers(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(user1.getEmail(), result.getContent().get(0).getEmail());
        assertEquals(user2.getEmail(), result.getContent().get(1).getEmail());
    }

    @Test
    void testFindUserById() {
        UUID userId = UUID.randomUUID();
        UserShowDTO user = new UserShowDTO(
                UUID.randomUUID(),
                "User",
                "user1@gmail.com",
                "ADMIN",
                LocalDateTime.of(2002, 10, 3, 0, 0),
                LocalDateTime.of(2002, 10, 3, 0, 0),  // Same for updatedTime or another value
                "creator1",
                "updater1"
        );

        when(userService.findUserById(userId)).thenReturn(user);

        UserShowDTO result = userService.findUserById(userId);

        assertEquals("ADMIN", result.getRole());
        assertEquals("user1@gmail.com", result.getEmail());
    }

    @Test
    void testFindUserByEmail() {
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);

        when(userService.findUserByEmail(email)).thenReturn(user);

        User result = userService.findUserByEmail(email);

        assertEquals(email, result.getEmail());
    }

    @Test
    void testCreateUser() {
        UserSaveDTO userSaveDTO = new UserSaveDTO("User","newuser@gmail.com", "Password123!","ADMIN");
        UserDTO createdUser = new UserDTO(UUID.randomUUID(),
                "User",
                "newuser@gmail.com",
                "ADMIN",
                LocalDateTime.of(2002, 10, 3, 0, 0),
                LocalDateTime.of(2002, 10, 3, 0, 0),  // Same for updatedTime or another value
                "creator1",
                "updater1");

        when(userService.createUser(userSaveDTO)).thenReturn(createdUser);

        UserDTO result = userService.createUser(userSaveDTO);

        assertNotNull(result.getId());
        assertEquals(userSaveDTO.getEmail(), result.getEmail());
    }

    @Test
    void testUpdateUser() {
        UUID userId = UUID.randomUUID();
        UserSaveDTO userSaveDTO = new UserSaveDTO("User", "updateduser@example.com", "Password123!", "ADMIN");
        UserDTO updatedUser = new UserDTO(UUID.randomUUID(),
                "User",
                "updateduser@example.com",
                "ADMIN",
                LocalDateTime.of(2002, 10, 3, 0, 0),
                LocalDateTime.of(2002, 10, 3, 0, 0),  // Same for updatedTime or another value
                "creator1",
                "updater1");

        when(userService.updateUser(userId, userSaveDTO)).thenReturn(updatedUser);

        UserDTO result = userService.updateUser(userId, userSaveDTO);

        assertEquals(userSaveDTO.getRole(), result.getRole());
        assertEquals(userSaveDTO.getEmail(), result.getEmail());
        assertEquals(userSaveDTO.getFullName(), result.getFullName());
    }

    @Test
    void testDeleteUser() {
        UUID userId = UUID.randomUUID();

        doNothing().when(userService).deleteUser(userId);

        userService.deleteUser(userId);

        verify(userService, times(1)).deleteUser(userId);
    }
}