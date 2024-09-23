package com.final_project_clinic.doctor.mapper;

import com.final_project_clinic.doctor.data.model.Doctor;
import com.final_project_clinic.doctor.dto.DoctorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "createdTime", source = "createdTime")
    @Mapping(target = "updatedBy", source = "updatedBy")
    @Mapping(target = "updatedTime", source = "updatedTime")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    DoctorDTO toDTO(Doctor doctor);

    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    Doctor toEntity(DoctorDTO doctorDTO);
}
