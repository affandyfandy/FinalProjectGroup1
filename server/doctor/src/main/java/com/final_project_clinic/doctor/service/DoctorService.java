package com.final_project_clinic.doctor.service;

import com.final_project_clinic.doctor.dto.DoctorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface DoctorService {

    Page<DoctorDTO> getAllDoctors(Pageable pageable);

    List<DoctorDTO> getAllDoctorsList();

    Optional<DoctorDTO> getDoctorById(UUID id);

    DoctorDTO createDoctor(DoctorDTO doctorDTO);

    Optional<DoctorDTO> editDoctor(UUID id, DoctorDTO doctorDTO);

    void deleteDoctor(UUID id);
}
