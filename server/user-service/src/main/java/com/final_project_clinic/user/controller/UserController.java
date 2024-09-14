package com.final_project_clinic.user.controller;

import com.final_project_clinic.user.dto.UserDTO;
import com.final_project_clinic.user.dto.UserSaveDTO;
import com.final_project_clinic.user.dto.UserShowDTO;
import com.final_project_clinic.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Retrieve a paginated list of users
    @GetMapping
    public ResponseEntity<Page<UserShowDTO>> getAllUsers(Pageable pageable) {
        Page<UserShowDTO> users = userService.findAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    // Retrieve a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserShowDTO> getUserById(@PathVariable UUID id) {
        UserShowDTO user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserSaveDTO userSaveDTO) {
        UserDTO newUser = userService.createUser(userSaveDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // Update an existing user
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable UUID id,
            @RequestBody UserSaveDTO userSaveDTO) {
        UserDTO updatedUser = userService.updateUser(id, userSaveDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
