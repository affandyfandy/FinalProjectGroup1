package com.final_project_clinic.user.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.final_project_clinic.user.data.model.User;
import com.final_project_clinic.user.data.repository.UserRepository;
import com.final_project_clinic.user.mapper.UserMapper;
import com.final_project_clinic.user.service.UserService;
import com.final_project_clinic.user.dto.UserDTO;
import com.final_project_clinic.user.dto.UserShowDTO;
import com.final_project_clinic.user.dto.UserSaveDTO;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
    public UserShowDTO findUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found."));
        return userMapper.toUserShowDTO(user);
    }

    @Override
    public UserDTO createUser(UserSaveDTO userSaveDTO) {
        User user = userMapper.toUser(userSaveDTO);
        user.setCreatedTime(new Date());
        user.setCreatedBy("admin"); // Replace with actual user from context
        user = userRepository.save(user);
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO updateUser(UUID id, UserSaveDTO userSaveDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found."));

        // Update fields from UserSaveDTO
        existingUser.setFull_name(userSaveDTO.getFull_name());
        existingUser.setEmail(userSaveDTO.getEmail());
        existingUser.setUpdatedTime(new Date());
        existingUser.setUpdatedBy("admin"); // Replace with actual user from context

        existingUser = userRepository.save(existingUser);
        return userMapper.toUserDTO(existingUser);
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found.");
        }
        userRepository.deleteById(id);
    }
}
