import { Component, OnInit } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { AppointmentService } from '../../../../services/appointment-service/appoinment.service';
import { AuthService } from '../../../../services/auth-service/auth.service';
import { PatientsService } from '../../../../services/patient-service/patients.service';
import { PatientShowDTO } from '../../../../models/patient.model';
import { CustomJwtPayload } from '../../../../models/user.model';
import { AppointmentShowDTO } from '../../../../models/appointment.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-appointment-history',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './appointment-history.component.html',
  styleUrl: './appointment-history.component.css'
})
export class AppointmentHistoryComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private patientsService: PatientsService,
    private appointmentService: AppointmentService,
  ) {}

  token: string = '';
  userId: string = '';
  patientId: string = '';
  patient: PatientShowDTO | null = null;
  appointments: AppointmentShowDTO[] = [];
  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile() {
    const token = this.authService.getToken();
    if (token) {
      this.token = token;
      const decoded = jwtDecode<CustomJwtPayload>(token);
      this.userId = decoded.id;

      if (this.userId) {
        this.patientsService.getPatientByUserId(this.userId).subscribe(
          (response) => {
            console.log(response);
            this.patient = response;
            this.patientId = this.patient.id;
            console.log('patinet', this.patientId);
            this.loadAppointment(this.patientId);
          },
          (error) => {
            this.authService.handleError(error);
            console.error('Error loading profile', error);
          }
        );
      }
    } else {
      console.error('Token is null');
    }
  }

  loadAppointment(patientId: string) {
    if (patientId) {
      this.appointmentService.getAppointmentByPatientId(patientId).subscribe(
        (response: AppointmentShowDTO[]) => {
          // Specify type here
          console.log(response);
          this.appointments = response;
         
        },
        (error) => {
          this.authService.handleError(error);
          console.error('Error loading appointments', error);
        }
      );
    }
  } 
}
