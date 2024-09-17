package com.final_project_clinic.doctor.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "DoctorSchedule")
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id", nullable = false)
    private Doctor doctor;

    @Column(name = "day", nullable = false)
    private String day;

    @OneToMany(mappedBy = "doctorSchedule", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ScheduleTime> scheduleTimes;
}
