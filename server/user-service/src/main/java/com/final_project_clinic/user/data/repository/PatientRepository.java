package com.final_project_clinic.user.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.final_project_clinic.user.data.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

}
