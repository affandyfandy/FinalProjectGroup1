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
  isLoading: boolean = true; // Track loading state
  hasNoData: boolean = false; // Track if there are no appointments

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
<<<<<<< HEAD
            console.log('patinet', this.patientId);
            this.loadAppointment(this.patientId, this.currentPage);;
=======
            console.log('Patient ID:', this.patientId);
            this.loadAppointment(this.patientId);
>>>>>>> dev
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

<<<<<<< HEAD
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
=======
  loadAppointment(patientId: string) {
    if (patientId) {
      this.appointmentService.getAppointmentByPatientId(patientId).subscribe(
        (response: AppointmentShowDTO[]) => {
          console.log(response);
          this.appointments = response;
          this.appointments = response.filter((app) => app.status !== 'ONGOING');
          this.hasNoData = this.appointments.length === 0;

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
              this.isLoading = false; // Set loading to false once data is fetched
            },
            (error) => {
              this.authService.handleError(error);
              console.error('Error loading doctor details', error);
              this.isLoading = false; // Ensure loading is set to false on error
            }
          );
        },
        (error) => {
          this.authService.handleError(error);
          console.error('Error loading appointments', error);
          this.isLoading = false; // Ensure loading is set to false on error
        }
      );
>>>>>>> dev
    }

<<<<<<< HEAD
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
=======
  getDoctorName(doctorId: string): string {
    console.log('Doctor Name:', this.doctorMap[doctorId]?.name);
    return this.doctorMap[doctorId]?.name || 'Unknown Doctor';
  }
>>>>>>> dev
}
