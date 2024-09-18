package com.final_project_clinic.doctor.mapper;

import com.final_project_clinic.doctor.data.model.Doctor;
import com.final_project_clinic.doctor.dto.DoctorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    DoctorDTO toDTO(Doctor doctor);

    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    Doctor toEntity(DoctorDTO doctorDTO);
}
