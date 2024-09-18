package com.final_project_clinic.doctor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableSpringDataWebSupport
@EnableMethodSecurity(prePostEnabled = true)
public class DoctorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorApplication.class, args);
	}

}
