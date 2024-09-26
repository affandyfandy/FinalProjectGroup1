import { Component, OnInit } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { AppointmentService } from '../../../../services/appointment-service/appoinment.service';
import { AuthService } from '../../../../services/auth-service/auth.service';
import { PatientsService } from '../../../../services/patient-service/patients.service';
import { PatientShowDTO } from '../../../../models/patient.model';
import { CustomJwtPayload } from '../../../../models/user.model';
import { AppointmentShowDTO } from '../../../../models/appointment.model';
import { CommonModule } from '@angular/common';
import { DoctorDTO } from '../../../../models/doctor.model';
import { DoctorsService } from '../../../../services/doctor-service/doctors.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-appointment-history',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './appointment-history.component.html',
  styleUrl: './appointment-history.component.css',
})
export class AppointmentHistoryComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private patientsService: PatientsService,
    private doctorsService: DoctorsService,
    private appointmentService: AppointmentService
  ) {}

  token: string = '';
  userId: string = '';
  patientId: string = '';
  patient: PatientShowDTO | null = null;
  appointments: AppointmentShowDTO[] = [];
  doctorMap: { [doctorId: string]: DoctorDTO } = {};

  currentPage: number = 1;
    pageSize: number = 6; // Adjust as needed
    totalItems: number = 0;
    totalPages: number = 0;

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
            this.loadAppointment(this.patientId, this.currentPage);;
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

  loadAppointment(patientId: string, page: number) {
      if (patientId) {
        this.appointmentService
          .getAppointmentByPatientIdWithPagination(
            patientId,
            page - 1, // Backend pagination usually starts at 0
            this.pageSize
          )
          .subscribe(
            (response) => {
              console.log(response);
              this.appointments = response.appointments;
              this.totalItems = response.total;
              this.totalPages = Math.ceil(this.totalItems / this.pageSize);

              // Fetch doctor details for each appointment
              const doctorRequests = this.appointments.map((appointment) =>
                this.doctorsService.getDoctorById(appointment.doctorId)
              );

              forkJoin(doctorRequests).subscribe(
                (doctors: DoctorDTO[]) => {
                  doctors.forEach((doctor, index) => {
                    console.log('Doctor:', doctor);
                    if (doctor) {
                      this.doctorMap[this.appointments[index].doctorId] = doctor;
                    }
                  });
                },
                (error) => {
                  this.authService.handleError(error);
                  console.error('Error loading doctor details', error);
                }
              );
            },
            (error) => {
              this.authService.handleError(error);
              console.error('Error loading appointments', error);
            }
          );
      }
    }

  onPageChange(page: number) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
        this.loadAppointment(this.patientId, this.currentPage);
      }
    }

    getDoctorName(doctorId: string): string {
      console.log('Doctor Name:', this.doctorMap[doctorId]?.name);
      return this.doctorMap[doctorId]?.name || 'Unknown Doctor';
    }
}
