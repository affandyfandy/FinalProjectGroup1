package com.final_project_clinic.user.mapper;

import com.final_project_clinic.user.dto.UserDTO;
import com.final_project_clinic.user.dto.UserShowDTO;
import com.final_project_clinic.user.dto.UserSaveDTO;
import com.final_project_clinic.user.data.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Map User entity to UserDTO
    UserDTO toUserDTO(User user);

    // Map User entity to UserShowDTO
    UserShowDTO toUserShowDTO(User user);

    // Map UserSaveDTO to User entity
    @Mapping(target = "id", ignore = true) // Ignore id when saving
    @Mapping(target = "createdTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedTime", ignore = true) // updatedTime will be set in the service layer
    @Mapping(target = "updatedBy", ignore = true) // updatedBy will be set in the service layer
    User toUser(UserSaveDTO userSaveDTO);

    // Map UserDTO back to User entity (if needed)
    @Mapping(target = "password", ignore = true)
    User toUser(UserDTO userDTO);
}
