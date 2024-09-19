package com.final_project_clinic.doctor.data.repository;

import com.final_project_clinic.doctor.data.model.ScheduleTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScheduleTimeRepository extends JpaRepository<ScheduleTime, UUID> {
}
