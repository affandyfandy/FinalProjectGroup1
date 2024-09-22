package com.final_project_clinic.appointment_services.mapper;

import com.final_project_clinic.appointment_services.data.model.Appointment;
import com.final_project_clinic.appointment_services.dto.AppointmentCreateDTO;
import com.final_project_clinic.appointment_services.dto.AppointmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "appointmentId", target = "appointmentId")
    @Mapping(source = "patientId", target = "patientId")
    @Mapping(source = "doctorId", target = "doctorId")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "initialComplaint", target = "initialComplaint")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "queueNumber", target = "queueNumber")
    AppointmentDTO toAppointmentDTO(Appointment appointment);

    Appointment toAppointment(AppointmentDTO appointmentDTO);
}