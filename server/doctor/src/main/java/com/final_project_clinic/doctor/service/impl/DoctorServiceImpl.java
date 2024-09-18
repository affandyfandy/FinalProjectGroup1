package com.final_project_clinic.doctor.service.impl;

import com.final_project_clinic.doctor.data.model.Doctor;
import com.final_project_clinic.doctor.data.repository.DoctorRepository;
import com.final_project_clinic.doctor.dto.DoctorDTO;
import com.final_project_clinic.doctor.exception.DuplicateIdentificationNumberException;
import com.final_project_clinic.doctor.mapper.DoctorMapper;
import com.final_project_clinic.doctor.service.DoctorService;
import com.final_project_clinic.doctor.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        Page<Doctor> doctors = doctorRepository.findAll(pageable);
        return doctors.map(doctorMapper::toDTO);
    }

    @Override
    public List<DoctorDTO> getAllDoctorsList() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(doctorMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<DoctorDTO> getDoctorById(UUID id) {
        return doctorRepository.findById(id).map(doctorMapper::toDTO);
    }

    @Override
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {

        Doctor existingDoctor = doctorRepository.findByIdentificationNumber(doctorDTO.getIdentificationNumber());
        if (existingDoctor != null) {
            throw new DuplicateIdentificationNumberException("Identification number already exists: " + doctorDTO.getIdentificationNumber());
        }

        ValidationUtils.validatePhoneNumber(doctorDTO.getPhoneNumber());

        Doctor doctor = doctorMapper.toEntity(doctorDTO);
        doctor.setId(UUID.randomUUID());
        doctor = doctorRepository.save(doctor);
        return doctorMapper.toDTO(doctor);
    }

    @Override
    public Optional<DoctorDTO> editDoctor(UUID id, DoctorDTO doctorDTO) {
        return doctorRepository.findById(id).map(doctor -> {
            Doctor existingDoctor = doctorRepository.findByIdentificationNumber(doctorDTO.getIdentificationNumber());
            if (existingDoctor != null) {
                throw new DuplicateIdentificationNumberException("Identification number already exists: " + doctorDTO.getIdentificationNumber());
            }

            ValidationUtils.validatePhoneNumber(doctorDTO.getPhoneNumber());

            doctor.setName(doctorDTO.getName());
            doctor.setSpecialization(doctorDTO.getSpecialization());
            doctor.setIdentificationNumber(doctorDTO.getIdentificationNumber());
            doctor.setPhoneNumber(doctorDTO.getPhoneNumber());
            doctor.setGender(doctorDTO.getGender());
            doctor.setDateOfBirth(doctorDTO.getDateOfBirth());
            doctor.setAddress(doctorDTO.getAddress());
            doctor.setPatientTotal(doctorDTO.getPatientTotal());
            doctorRepository.save(doctor);
            return doctorMapper.toDTO(doctor);
        });
    }

    @Override
    public void deleteDoctor(UUID id) {
        doctorRepository.deleteById(id);
    }
}
