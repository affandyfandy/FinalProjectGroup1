package com.final_project_clinic.doctor.mapper;

import com.final_project_clinic.doctor.data.model.DoctorSchedule;
import com.final_project_clinic.doctor.data.model.ScheduleTime;
import com.final_project_clinic.doctor.dto.ScheduleTimeDTO;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { DoctorScheduleMapper.class })
public interface ScheduleTimeMapper {

    ScheduleTimeDTO toScheduleTimeDTO(ScheduleTime scheduleTime);

    @Mapping(target = "doctorSchedule", ignore = true)
    ScheduleTime toScheduleTime(ScheduleTimeDTO scheduleTimeDTO, @Context DoctorSchedule doctorSchedule);
}
