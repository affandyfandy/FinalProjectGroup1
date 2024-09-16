package com.final_project_clinic.user.dto;

import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserShowDTO {
    private UUID id;
    private String full_name;
    private String email;
    private String role;
    private Date createdTime;
}