package com.final_project_clinic.user.mapper;

import com.final_project_clinic.user.data.model.User;
import com.final_project_clinic.user.dto.UserDTO;
import com.final_project_clinic.user.dto.UserSaveDTO;
import com.final_project_clinic.user.dto.UserShowDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToUserDTO() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFullName("johndoe");
        user.setPassword("Password123!");
        user.setCreatedTime(LocalDateTime.now());

        UserDTO userDTO = userMapper.toUserDTO(user);

        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getFullName(), userDTO.getFullName());
        assertEquals(user.getCreatedTime(), userDTO.getCreatedTime());
    }

    @Test
    void testToUserShowDTO() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFullName("janedoe");
        user.setPassword("Password123!");
        user.setCreatedTime(LocalDateTime.now());

        UserShowDTO userShowDTO = userMapper.toUserShowDTO(user);

        assertEquals(user.getId(), userShowDTO.getId());
        assertEquals(user.getFullName(), userShowDTO.getFullName());
        assertEquals(user.getCreatedTime(), userShowDTO.getCreatedTime());
    }

    @Test
    void testToUserFromUserSaveDTO() {
        UserSaveDTO userSaveDTO = new UserSaveDTO();
        userSaveDTO.setFullName("Alice Pop");
        userSaveDTO.setPassword("Password123");

        User user = userMapper.toUser(userSaveDTO);

        assertNull(user.getId()); // Ensure id is not set
        assertEquals(userSaveDTO.getFullName(), user.getFullName());
        // Password should be set from DTO
        assertEquals(userSaveDTO.getPassword(), user.getPassword());
        // CreatedTime should be set to current time
        assertNotNull(user.getCreatedTime());
        assertNull(user.getUpdatedTime()); // Ensure updatedTime is not set
        assertNull(user.getUpdatedBy()); // Ensure updatedBy is not set
    }

    @Test
    void testToUserFromUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setFullName("johndoe");
        userDTO.setCreatedTime(LocalDateTime.now());

        User user = userMapper.toUser(userDTO);

        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getFullName(), user.getFullName());
        // Password should be ignored
        assertNull(user.getPassword());
    }
}
