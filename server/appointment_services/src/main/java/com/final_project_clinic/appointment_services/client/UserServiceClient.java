package com.final_project_clinic.appointment_services.client;

import com.final_project_clinic.appointment_services.config.FeignClientConfig;
import com.final_project_clinic.appointment_services.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@FeignClient(name = "user-services", url = "http://localhost:8080", configuration = FeignClientConfig.class)
public interface UserServiceClient {

    @GetMapping("/api/v1/patients/{userId}")
    UserDTO getUserById(@PathVariable("userId") UUID userId);
}
