package com.final_project_clinic.appointment_services.client;

import com.final_project_clinic.appointment_services.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@FeignClient(name = "user-services")
public interface UserServiceClient {

    @GetMapping("/users/{userId}")
    UserDTO getUserById(@PathVariable("userId") UUID userId);
}
