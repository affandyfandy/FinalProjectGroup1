import { Component, OnInit, Input } from '@angular/core';
import { AppointmentService } from '../../../../services/appointment-service/appoinment.service';
import { DoctorsService } from '../../../../services/doctor-service/doctors.service';
import { PatientsService } from '../../../../services/patient-service/patients.service';
import { ToastrService } from 'ngx-toastr';
import { AppointmentShowDTO } from '../../../../models/appointment.model';
import { CommonModule } from '@angular/common';
import { TimeOnlyPipe } from '../../../../core/pipes/time-only.pipe';
@Component({
  selector: 'app-appointment-modal',
  standalone: true,
  imports: [CommonModule, TimeOnlyPipe],
  templateUrl: './appointment-modal.component.html',
  styleUrls: ['./appointment-modal.component.css'],
})
export class AppointmentModalComponent implements OnInit {
  @Input() appointment!: AppointmentShowDTO;
  patientName: string = '';
  doctorName: string = '';

  constructor(
    private appointmentService: AppointmentService,
    private patientService: PatientsService,
    private doctorsService: DoctorsService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    if (this.appointment) {
      this.loadPatientName();
      this.loadDoctorName();
    }
  }

  loadPatientName() {
    this.patientService.getPatients().subscribe({
      next: (patients) => {
        const patient = patients.content.find(
          (p: any) => p.id === this.appointment.patientId
        );
        this.patientName = patient?.user?.fullName || 'Unknown Patient';
      },
      error: (error) => {
        console.error('Error loading patient name:', error);
      },
    });
  }

  loadDoctorName() {
    this.doctorsService.getDoctors().subscribe({
      next: (doctors) => {
        const doctor = doctors.find((d) => d.id === this.appointment.doctorId);
        this.doctorName = doctor?.name || 'Unknown Doctor';
      },
      error: (error) => {
        console.error('Error loading doctor name:', error);
      },
    });
  }
}
