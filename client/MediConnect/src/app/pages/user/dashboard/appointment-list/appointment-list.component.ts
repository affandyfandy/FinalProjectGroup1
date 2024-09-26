import { Component, ViewChild, OnInit } from '@angular/core';
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
import {
  HlmCaptionComponent,
  HlmTableComponent,
  HlmTdComponent,
  HlmThComponent,
  HlmTrowComponent,
} from '@spartan-ng/ui-table-helm';

import {
  HlmAvatarImageDirective,
  HlmAvatarComponent,
  HlmAvatarFallbackDirective,
} from '@spartan-ng/ui-avatar-helm';

import {
  BrnAlertDialogContentDirective,
  BrnAlertDialogTriggerDirective,
} from '@spartan-ng/ui-alertdialog-brain';
import {
  HlmAlertDialogActionButtonDirective,
  HlmAlertDialogCancelButtonDirective,
  HlmAlertDialogComponent,
  HlmAlertDialogContentComponent,
  HlmAlertDialogDescriptionDirective,
  HlmAlertDialogFooterComponent,
  HlmAlertDialogHeaderComponent,
  HlmAlertDialogOverlayDirective,
  HlmAlertDialogTitleDirective,
} from '@spartan-ng/ui-alertdialog-helm';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { TimeOnlyPipe } from '../../../../core/pipes/time-only.pipe';
@Component({
  selector: 'app-appointment-list',
  standalone: true,
  imports: [
    CommonModule,
    HlmTableComponent,
    HlmTrowComponent,
    HlmThComponent,
    HlmTdComponent,
    HlmCaptionComponent,

    HlmAvatarImageDirective,
    HlmAvatarComponent,
    HlmAvatarFallbackDirective,

    CommonModule,
    HlmTableComponent,
    HlmTrowComponent,
    HlmThComponent,
    HlmTdComponent,
    HlmCaptionComponent,

    HlmAvatarImageDirective,
    HlmAvatarComponent,
    HlmAvatarFallbackDirective,

    BrnAlertDialogContentDirective,
    BrnAlertDialogTriggerDirective,
    HlmAlertDialogActionButtonDirective,
    HlmAlertDialogCancelButtonDirective,
    HlmAlertDialogComponent,
    HlmAlertDialogContentComponent,
    HlmAlertDialogDescriptionDirective,
    HlmAlertDialogFooterComponent,
    HlmAlertDialogHeaderComponent,
    HlmAlertDialogOverlayDirective,
    HlmAlertDialogTitleDirective,

    TimeOnlyPipe,
  ],
  templateUrl: './appointment-list.component.html',
  styleUrl: './appointment-list.component.css',
})
export class AppointmentListComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private patientsService: PatientsService,
    private doctorsService: DoctorsService,
    private appointmentService: AppointmentService,
    private toastr: ToastrService
  ) {}

  @ViewChild(ToastContainerDirective, { static: true })
  toastContainer!: ToastContainerDirective;

  token: string = '';
  userId: string = '';
  patientId: string = '';
  patient: PatientShowDTO | null = null;
  appointments: AppointmentShowDTO[] = [];
  doctorMap: { [doctorId: string]: DoctorDTO } = {};

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
          // Specify type here
          console.log(response);
          this.appointments = response;
          this.appointments = response.filter((app) => app.status !== 'CANCEL');
          const doctorRequests = this.appointments.map((appointment) =>
            this.doctorsService.getDoctorById(appointment.doctorId)
          );

          forkJoin(doctorRequests).subscribe(
            (doctors: DoctorDTO[]) => {
              doctors.forEach((doctor, index) => {
                console.log('AAAA', doctor);
                if (doctor) {
                  this.doctorMap[this.appointments[index].doctorId] = doctor;
                }
              });
            },
            (error) => {
              // this.authService.handleError(error);
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

  getDoctorName(doctorId: string): string {
    console.log('Dwqdq', this.doctorMap[doctorId]?.name);
    return this.doctorMap[doctorId]?.name || 'Unknown Doctor';
  }

  cancelAppointment(appointmentId: string, ctx: any): void {
    this.appointmentService.cancelAppointment(appointmentId).subscribe({
      next: () => {
        this.toastr.success('Success cancel appointment');
        ctx.close();
        // this.loadAppointment(); // Reload products after deletion
      },
      error: (e) => {
        this.toastr.error('Failed to cancel');
        ctx.close();
      },
    });
  }
}
