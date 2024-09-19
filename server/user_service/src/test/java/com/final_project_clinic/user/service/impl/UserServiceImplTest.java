package com.final_project_clinic.user.service.impl;

import com.final_project_clinic.user.data.model.Role;
import com.final_project_clinic.user.data.model.User;
import com.final_project_clinic.user.data.repository.UserRepository;
import com.final_project_clinic.user.dto.UserDTO;
import com.final_project_clinic.user.dto.UserSaveDTO;
import com.final_project_clinic.user.dto.UserShowDTO;
import com.final_project_clinic.user.exception.DuplicateEmailException;
import com.final_project_clinic.user.exception.ResourceNotFoundException;
import com.final_project_clinic.user.mapper.UserMapper;
import com.final_project_clinic.user.utils.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserSaveDTO userSaveDTO;
    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userSaveDTO = new UserSaveDTO();
        userSaveDTO.setEmail("test@example.com");
        userSaveDTO.setFullName("John Doe");
        userSaveDTO.setPassword("password123");

        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(userSaveDTO.getEmail());
        user.setFullName(userSaveDTO.getFullName());
        user.setPassword(PasswordUtils.hashPassword(userSaveDTO.getPassword()));

        userDTO = new UserDTO();
        userDTO.setEmail(userSaveDTO.getEmail());
        userDTO.setFullName(userSaveDTO.getFullName());
    }

    @Test
    void testFindAllUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        User user1 = new User();
        User user2 = new User();
        Page<User> userPage = new PageImpl<>(Arrays.asList(user1, user2));

        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapper.toUserShowDTO(any(User.class))).thenReturn(new UserShowDTO());

        Page<UserShowDTO> result = userService.findAllUsers(pageable);

        assertEquals(2, result.getContent().size());
        verify(userRepository).findAll(pageable);
        verify(userMapper, times(2)).toUserShowDTO(any(User.class));
    }

    @Test
    void testFindUserByEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        User result = userService.findUserByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testFindUserById() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toUserShowDTO(user)).thenReturn(new UserShowDTO());

        UserShowDTO result = userService.findUserById(id);

        assertNotNull(result);
        verify(userRepository).findById(id);
        verify(userMapper).toUserShowDTO(user);
    }

    @Test
    void testFindUserByIdNotFound() {
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findUserById(id));
        verify(userRepository).findById(id);
    }

    @Test
    void testCreateUser() {
        UserSaveDTO userSaveDTO = new UserSaveDTO();
        userSaveDTO.setEmail("test@example.com");
        userSaveDTO.setPassword("password");
        userSaveDTO.setRole(Role.PATIENT.toString());

        User user = new User();
        user.setId(UUID.randomUUID());

        when(userMapper.toUser(userSaveDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserDTO(user)).thenReturn(new UserDTO());

        UserDTO result = userService.createUser(userSaveDTO);

        assertNotNull(result);
        verify(userMapper).toUser(userSaveDTO);
        verify(userRepository).save(any(User.class));
        verify(userMapper).toUserDTO(user);
    }
    @Test
    void testCreateUserWithUniqueEmail() {
        when(userRepository.findByEmail(userSaveDTO.getEmail())).thenReturn(null);
        when(userMapper.toUser(userSaveDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userSaveDTO);

        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
        verify(userRepository).findByEmail(userSaveDTO.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void testCreateUserWithDuplicateEmail() {
        User existingUser = new User();
        existingUser.setEmail(userSaveDTO.getEmail());

        when(userRepository.findByEmail(userSaveDTO.getEmail())).thenReturn(existingUser);

        DuplicateEmailException thrown = assertThrows(
                DuplicateEmailException.class,
                () -> userService.createUser(userSaveDTO)
        );

        assertEquals("Email already exists: " + userSaveDTO.getEmail(), thrown.getMessage());
        verify(userRepository).findByEmail(userSaveDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUserWithDuplicateEmail() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@example.com");

        User userWithEmail = new User();
        userWithEmail.setId(UUID.randomUUID());
        userWithEmail.setEmail(userSaveDTO.getEmail());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(userSaveDTO.getEmail())).thenReturn(userWithEmail);

        DuplicateEmailException thrown = assertThrows(
                DuplicateEmailException.class,
                () -> userService.updateUser(userId, userSaveDTO)
        );

        assertEquals("Email already exists: " + userSaveDTO.getEmail(), thrown.getMessage());
        verify(userRepository).findById(userId);
        verify(userRepository).findByEmail(userSaveDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        UUID id = UUID.randomUUID();
        UserSaveDTO userSaveDTOs = new UserSaveDTO();
        userSaveDTOs.setFullName("Updated Name");
        userSaveDTOs.setEmail("updated@example.com");

        User existingUser = new User();
        existingUser.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.toUserDTO(existingUser)).thenReturn(new UserDTO());

        UserDTO result = userService.updateUser(id, userSaveDTOs);

        assertNotNull(result);
        verify(userRepository).findById(id);
        verify(userRepository).save(existingUser);
        verify(userMapper).toUserDTO(existingUser);
    }

    @Test
    void testUpdateUserNotFound() {
        UUID id = UUID.randomUUID();
        UserSaveDTO userSaveDTO = new UserSaveDTO();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(id, userSaveDTO));
        verify(userRepository).findById(id);
    }

    @Test
    void testDeleteUser() {
        UUID id = UUID.randomUUID();

        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUser(id);

        verify(userRepository).existsById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    void testDeleteUserNotFound() {
        UUID id = UUID.randomUUID();

        when(userRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(id));
        verify(userRepository).existsById(id);
    }
}