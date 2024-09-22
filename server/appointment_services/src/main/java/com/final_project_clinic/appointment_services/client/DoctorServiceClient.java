package com.final_project_clinic.appointment_services.client;

import com.final_project_clinic.appointment_services.config.FeignClientConfig;
import com.final_project_clinic.appointment_services.dto.DoctorScheduleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@FeignClient(name = "doctor-service", url = "http://localhost:8080",configuration = FeignClientConfig.class)
public interface DoctorServiceClient {

    @GetMapping("/api/v1/schedules/doctor/{doctorId}/day/{day}")
    DoctorScheduleDTO getDoctorScheduleByDay(@PathVariable("doctorId") UUID doctorId,
                                             @PathVariable("day") String day);
}

