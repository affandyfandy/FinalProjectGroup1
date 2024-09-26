package com.final_project_clinic.doctor.data.repository;

import com.final_project_clinic.doctor.data.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;
import java.util.*;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {

    @Query("SELECT ds FROM DoctorSchedule ds WHERE (:doctorName IS NULL OR ds.doctor.name = :doctorName) " +
            "AND (:specialization IS NULL OR ds.doctor.specialization = :specialization) " +
            "AND (:dayOfWeek IS NULL OR UPPER(ds.day) = :dayOfWeek)")
    List<DoctorSchedule> findByCriteria(
            @Param("doctorName") String doctorName,
            @Param("specialization") String specialization,
            @Param("dayOfWeek") String dayOfWeek);

    Optional<DoctorSchedule> findByDoctorIdAndDay(UUID doctorId, String day);
}
