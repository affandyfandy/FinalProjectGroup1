package com.final_project_clinic.doctor.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "ScheduleTime")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ScheduleTimeId.class)
public class ScheduleTime extends Audit {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id", nullable = false)
    private DoctorSchedule doctorSchedule;

    @Id
    @Column(name = "start_working_hour", nullable = false)
    private LocalTime startWorkingHour;

    @Column(name = "end_working_hour", nullable = false)
    private LocalTime endWorkingHour;

    @Column(name = "max_patient", nullable = false)
    private Integer maxPatient;
}
