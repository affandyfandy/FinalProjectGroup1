package com.final_project_clinic.user.mapper;

import com.final_project_clinic.user.dto.PatientDTO;
import com.final_project_clinic.user.dto.PatientShowDTO;
import com.final_project_clinic.user.dto.PatientSaveDTO;
import com.final_project_clinic.user.data.model.Patient;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    // Map Patient to PatientDTO
    PatientDTO toPatientDTO(Patient patient);

    // Map PatientDTO to Patient
    Patient toPatient(PatientDTO patientDTO);

    // Map Patient to PatientShowDTO
    PatientShowDTO toPatientShowDTO(Patient patient);

    // Map PatientSaveDTO to Patient
    @Mapping(target = "id", ignore = true) // id is auto-generated
    @Mapping(target = "createdTime", ignore = true) // createdTime will be set in the service layer
    @Mapping(target = "updatedTime", ignore = true) // updatedTime will be set in the service layer
    @Mapping(target = "updatedBy", ignore = true) // updatedBy will be set in the service layer
    Patient toPatient(PatientSaveDTO patientSaveDTO);
}
