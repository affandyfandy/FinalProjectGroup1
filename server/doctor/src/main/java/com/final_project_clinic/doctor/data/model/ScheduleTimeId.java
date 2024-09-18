package com.final_project_clinic.doctor.data.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ScheduleTimeId implements Serializable {

    private UUID doctorSchedule;
    private LocalTime startWorkingHour;
}
