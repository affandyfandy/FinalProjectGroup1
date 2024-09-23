package com.final_project_clinic.doctor.mapper;

import com.final_project_clinic.doctor.data.model.Doctor;
import com.final_project_clinic.doctor.data.model.DoctorSchedule;
import com.final_project_clinic.doctor.dto.DoctorScheduleDTO;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = { DoctorMapper.class, ScheduleTimeMapper.class })
public interface DoctorScheduleMapper {

    DoctorScheduleMapper INSTANCE = Mappers.getMapper(DoctorScheduleMapper.class);

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "scheduleTimes", target = "scheduleTimes")
    DoctorScheduleDTO toDoctorScheduleDTO(DoctorSchedule doctorSchedule);

    @Mapping(source = "doctorId", target = "doctor.id")
    @Mapping(source = "scheduleTimes", target = "scheduleTimes")
    DoctorSchedule toDoctorSchedule(DoctorScheduleDTO doctorScheduleDTO, @Context Doctor doctor);


}
