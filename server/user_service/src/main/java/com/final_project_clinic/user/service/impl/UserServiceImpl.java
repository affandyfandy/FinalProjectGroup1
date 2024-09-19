package com.final_project_clinic.user.service.impl;

import java.util.UUID;

import com.final_project_clinic.user.exception.DuplicateEmailException;
import com.final_project_clinic.user.exception.DuplicateNikException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.final_project_clinic.user.data.model.Role;
import com.final_project_clinic.user.data.model.User;
import com.final_project_clinic.user.data.repository.UserRepository;
import com.final_project_clinic.user.mapper.UserMapper;
import com.final_project_clinic.user.service.UserService;
import com.final_project_clinic.user.utils.PasswordUtils;
import com.final_project_clinic.user.dto.UserDTO;
import com.final_project_clinic.user.dto.UserShowDTO;
import com.final_project_clinic.user.exception.ResourceNotFoundException;
import com.final_project_clinic.user.dto.UserSaveDTO;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final String USER_NOT_FOUND = "User Not Found";

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Page<UserShowDTO> findAllUsers(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<User> users = userRepository.findAll(sortedPageable);
        return users.map(userMapper::toUserShowDTO);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserShowDTO findUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        return userMapper.toUserShowDTO(user);
    }

    @Override
    public UserDTO createUser(UserSaveDTO userSaveDTO) {
        // Check for existing user with the same email
        User existingUser = userRepository.findByEmail(userSaveDTO.getEmail());
        if (existingUser != null) {
            throw new DuplicateEmailException("Email already exists: " + userSaveDTO.getEmail());
        }

        User user = userMapper.toUser(userSaveDTO);
        String role = (userSaveDTO.getRole() != null && !userSaveDTO.getRole().isEmpty())
                ? userSaveDTO.getRole()
                : Role.PATIENT.toString();

        user.setRole(role);
        user.setPassword(PasswordUtils.hashPassword(userSaveDTO.getPassword()));
        user = userRepository.save(user);
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO updateUser(UUID id, UserSaveDTO userSaveDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        // Check if another user with the same email exists (excluding the current user)
        User userWithEmail = userRepository.findByEmail(userSaveDTO.getEmail());
        if (userWithEmail != null && !userWithEmail.getId().equals(id)) {
            throw new DuplicateEmailException("Email already exists: " + userSaveDTO.getEmail());
        }

        // Update fields from UserSaveDTO
        existingUser.setFullName(userSaveDTO.getFullName());
        existingUser.setEmail(userSaveDTO.getEmail());
        existingUser = userRepository.save(existingUser);
        return userMapper.toUserDTO(existingUser);
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}
