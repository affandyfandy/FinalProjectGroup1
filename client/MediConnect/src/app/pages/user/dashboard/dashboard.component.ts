import { Component, OnInit } from '@angular/core';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { AppointmentListComponent } from './appointment-list/appointment-list.component';
import { QueueListComponent } from './queue-list/queue-list.component';
import { AppointmentSearchFormComponent } from './appointment-search-form/appointment-search-form.component';
import { AuthService } from '../../../services/auth-service/auth.service';
import { Router, RouterModule } from '@angular/router';
import { AppointmentService } from '../../../services/appointment-service/appoinment.service';
import { AppointmentShowDTO } from '../../../models/appointment.model';
import { PatientsService } from '../../../services/patient-service/patients.service';
import { CustomJwtPayload } from '../../../models/user.model';
import { jwtDecode } from 'jwt-decode';
import { PatientShowDTO } from '../../../models/patient.model';
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    RouterModule,
    HlmButtonDirective,
    AppointmentListComponent,
    AppointmentSearchFormComponent,
    QueueListComponent,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  username = ''; // Store the username
  onGoingAppointment: AppointmentShowDTO[] | null = null;
  onGoingAppointmentCount: number = 0; // To store the count
  token: string = '';
  userId: string = '';
  patientId: string = '';
  patient: PatientShowDTO | null = null;

  constructor(
    private router: Router,
    private authService: AuthService,
    private patientsService: PatientsService,
    private appointmentService: AppointmentService
  ) {}

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
            // this.authService.handleError(error);
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
          console.log(response);

          // Filter appointments with status 'OnGoing'
          this.onGoingAppointment = response.filter(
            (app) => app.status === 'ONGOING'
          );

          // Get the length of onGoingAppointment
          this.onGoingAppointmentCount = this.onGoingAppointment.length;
          console.log(
            'Number of ongoing appointments:',
            this.onGoingAppointmentCount
          );
        },
        (error) => {
          this.authService.handleError(error);
          console.error('Error loading appointments', error);
        }
      );
    }
  }
}
