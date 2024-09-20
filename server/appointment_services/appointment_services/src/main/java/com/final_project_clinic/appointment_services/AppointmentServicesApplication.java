package com.final_project_clinic.appointment_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class AppointmentServicesApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppointmentServicesApplication.class, args);
	}
}
