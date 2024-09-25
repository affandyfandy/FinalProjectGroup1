package com.final_project_clinic.user.controller;

import com.final_project_clinic.user.dto.UserDTO;
import com.final_project_clinic.user.dto.UserSaveDTO;
import com.final_project_clinic.user.dto.UserShowDTO;
import com.final_project_clinic.user.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/public")
    public ResponseEntity<Page<UserShowDTO>> getAllUsersPublic(Pageable pageable) {
        Page<UserShowDTO> users = userService.findAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    // Retrieve a paginated list of users (Only SUPERADMIN can view)
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserShowDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserShowDTO> users = userService.findAllUsers(pageable);
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(users);
    }

    // Retrieve a user by ID (Only SUPERADMIN can view)
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserShowDTO> getUserById(@PathVariable UUID id) {
        UserShowDTO user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    // @PreAuthorize("hasAuthority('PATIENT','ADMIN','SUPERADMIN')")
    @GetMapping("/patient/{id}")
    public ResponseEntity<UserShowDTO> getUserPatientById(@PathVariable UUID id) {
        UserShowDTO user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    // Create a new user (Only SUPERADMIN can create)
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserSaveDTO userSaveDTO) {
        UserDTO newUser = userService.createUser(userSaveDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @PutMapping("/patient/{id}")
    public ResponseEntity<UserDTO> updateUserPatient(
            @PathVariable UUID id,
            @RequestBody @Valid UserSaveDTO userSaveDTO) {
        UserDTO updatedUser = userService.updateUser(id, userSaveDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Update an existing user (Only SUPERADMIN can update)
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable UUID id,
            @RequestBody @Valid UserSaveDTO userSaveDTO) {
        UserDTO updatedUser = userService.updateUser(id, userSaveDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete a user by ID (Only SUPERADMIN can delete)
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
