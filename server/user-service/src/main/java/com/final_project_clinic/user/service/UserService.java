package com.final_project_clinic.user.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.final_project_clinic.user.dto.UserDTO;
import com.final_project_clinic.user.dto.UserSaveDTO;
import com.final_project_clinic.user.dto.UserShowDTO;

public interface UserService {

    // Find patients based on the provided criteria.
    Page<UserShowDTO> findAllUsers(Pageable pageable);

    // Find User by its id
    UserShowDTO findUserById(UUID id);

    // Creating a new User.
    UserDTO createUser(UserSaveDTO userSaveDTO);

    // Updates an existing User with the provided User details.
    UserDTO updateUser(UUID id, UserSaveDTO userSaveDTO);

    // Delete a User data by id.
    void deleteUser(UUID id);

}
