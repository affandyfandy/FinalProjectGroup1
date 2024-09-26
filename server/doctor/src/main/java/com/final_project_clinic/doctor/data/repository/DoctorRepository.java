package com.final_project_clinic.doctor.data.repository;

import com.final_project_clinic.doctor.data.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Doctor findByIdentificationNumber(String identificationNumber);
    boolean existsByIdentificationNumber(String identificationNumber);

    @Query("SELECT DISTINCT d FROM Doctor d JOIN d.schedules s")
    Page<Doctor> findAllWithSchedules(Pageable pageable);

}
