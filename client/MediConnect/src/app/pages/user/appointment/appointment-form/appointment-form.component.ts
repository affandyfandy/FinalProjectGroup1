import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
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
import { MessageErrorRequiredField } from '../../../../utils/message';
import { CommonModule } from '@angular/common';
import { DoctorSchedulesService } from '../../../../services/doctor-schedule-service/doctor-schedules.service';
import { AuthService } from '../../../../services/auth-service/auth.service';
import { CustomJwtPayload, User } from '../../../../models/user.model';
import { PatientsService } from '../../../../services/patient-service/patients.service';
import { PatientShowDTO } from '../../../../models/patient.model';
import { AppointmentService } from '../../../../services/appointment-service/appoinment.service';
import { AppointmentSaveDTO } from '../../../../models/appointment.model';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-appointment-form',
  standalone: true,
  imports: [
    RouterModule,
    CommonModule,
    ReactiveFormsModule,
    BrnAlertDialogTriggerDirective,
    BrnAlertDialogContentDirective,
    HlmAlertDialogComponent,
    HlmAlertDialogOverlayDirective,
    HlmAlertDialogHeaderComponent,
    HlmAlertDialogFooterComponent,
    HlmAlertDialogTitleDirective,
    HlmAlertDialogDescriptionDirective,
    HlmAlertDialogCancelButtonDirective,
    HlmAlertDialogActionButtonDirective,
    HlmAlertDialogContentComponent,
  ],
  templateUrl: './appointment-form.component.html',
  styleUrl: './appointment-form.component.css',
})
export class AppointmentFormComponent implements OnInit {
  appointmentForm: FormGroup;
  messageErrorRequiredField = MessageErrorRequiredField;
  @Input() dialogCtx: any;
  @Input() doctorId: string = '';
  selectedDay: string = '';
  scheduleTimes: any[] = [];
  users: User | null = null;
  patient: PatientShowDTO | null = null;
  token: string = '';
  userId: string = '';
  patientId: string = '';

  @ViewChild(ToastContainerDirective, { static: true })
  toastContainer!: ToastContainerDirective;

  // New flag to prevent double submission
  isSubmitting = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private doctorScheduleService: DoctorSchedulesService,
    private authService: AuthService,
    private patientsService: PatientsService,
    private appointmentService: AppointmentService,
    private toastrService: ToastrService
  ) {
    this.appointmentForm = this.fb.group({
      date: ['', Validators.required],
      hour: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      initialComplaint: ['', [Validators.required, Validators.maxLength(250)]],
    });
  }

  ngOnInit(): void {
    this.appointmentForm.get('date')?.valueChanges.subscribe((selectedDate) => {
      const day = this.getDayFromDate(selectedDate);
      this.selectedDay = day;
      this.loadScheduleByDay(this.doctorId, this.selectedDay);
    });

    this.appointmentForm.get('hour')?.valueChanges.subscribe((selectedHour) => {
      this.updateStartEndTime(selectedHour);
    });

    this.loadProfile();
  }

  getDayFromDate(dateString: string): string {
    const date = new Date(dateString);
    const days = [
      'Sunday',
      'Monday',
      'Tuesday',
      'Wednesday',
      'Thursday',
      'Friday',
      'Saturday',
    ];
    return days[date.getUTCDay()];
  }

  loadScheduleByDay(doctorId: string, day: string) {
    this.doctorScheduleService
      .getSchedulesDoctorByDay(doctorId, day)
      .subscribe({
        next: (response) => {
          if (
            response &&
            response.scheduleTimes &&
            response.scheduleTimes.length
          ) {
            this.scheduleTimes = response.scheduleTimes;
          }
        },
        error: (error) => {
          console.error('Error loading doctor schedules:', error);
        },
      });
  }

  updateStartEndTime(selectedHour: string) {
    const selectedSchedule = this.scheduleTimes.find(
      (schedule) =>
        `${schedule.startWorkingHour} - ${schedule.endWorkingHour}` ===
        selectedHour
    );
    if (selectedSchedule) {
      this.appointmentForm.patchValue({
        startTime: selectedSchedule.startWorkingHour,
        endTime: selectedSchedule.endWorkingHour,
      });
    }
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
            this.patient = response;
            this.patientId = this.patient.id;
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

  // Method to handle form submission
  submitForm(): boolean {
    // Prevent double submission
    if (this.isSubmitting || this.appointmentForm.invalid) {
      return false;
    }

    // Prepare the form data to align with AppointmentSaveDTO interface
    const formData: AppointmentSaveDTO = {
      doctorId: this.doctorId,
      patientId: this.patientId,
      initialComplaint: this.appointmentForm.value.initialComplaint,
      date: this.appointmentForm.value.date,
      startTime: this.appointmentForm.value.startTime,
      endTime: this.appointmentForm.value.endTime,
    };

    this.isSubmitting = true; // Set the flag to true to indicate submission

    // Call the appointment service to create a new appointment
    this.appointmentService.createAppointment(formData).subscribe({
      next: () => {
        this.toastrService.success(
          'Welcome Back!!',
          'Appointment created successfully!'
        );
        this.isSubmitting = false; // Reset the flag
        this.router.navigate(['/dashboard']);
      },
      error: (error: any) => {
        this.toastrService.error(
          'Try Again!!',
          // error.error || this.messageInvalidValidation
          'You already have an appointment with this doctor at the same time'
        );

        console.error('Error:', error);
        this.isSubmitting = false; // Reset the flag
      },
    });

    return true;
  }

  // Method to handle the confirmation dialog and close the parent dialog
  confirmAndSubmit(ctx: any, dialogCtx: any) {
    if (this.submitForm()) {
      ctx.close(); // Close the confirmation dialog
      dialogCtx?.close(); // Close the parent dialog
    }
  }

  get date() {
    return this.appointmentForm.get('date');
  }
  get hour() {
    return this.appointmentForm.get('hour');
  }
  get initialComplaint() {
    return this.appointmentForm.get('initialComplaint');
  }
}
