package com.final_project_clinic.doctor.data.repository;

import com.final_project_clinic.doctor.data.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {

    Optional<DoctorSchedule> findByDoctorIdAndDay(UUID doctorId, String day);
}
