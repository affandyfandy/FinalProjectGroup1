import { Component, OnInit, Input } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorSchedulesService } from '../../../../services/doctor-schedule-service/doctor-schedules.service';
import { DoctorsService } from '../../../../services/doctor-service/doctors.service';
import { AppointmentService } from '../../../../services/appointment-service/appoinment.service';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { PatientsService } from '../../../../services/patient-service/patients.service';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';

interface AppointmentSaveDTO {
  doctorId: string;
  patientId: string;
  initialComplaint: string;
  date: string;
  startTime: string;
  endTime: string;
}

@Component({
  selector: 'app-appointment-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HlmInputDirective,
    HlmButtonDirective,
    HlmLabelDirective,
  ],
  templateUrl: './appointment-form.component.html',
  styleUrls: ['./appointment-form.component.css'],
})
export class AppointmentFormComponent implements OnInit {
  @Input() isEditMode: boolean = false;
  @Input() id: string | null = null;
  appointmentForm: FormGroup;
  doctors: any[] = [];
  patients: any[] = [];
  scheduleTimes: any[] = [];
  isSubmitting: boolean = false;
  doctorId: string | null = null;
  patientId: string | null = null;
  selectedDay: string = '';
  today: string;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private doctorScheduleService: DoctorSchedulesService,
    private patientService: PatientsService,
    private doctorService: DoctorsService,
    private appointmentService: AppointmentService, // Appointment service for creating/updating appointments
    private toastr: ToastrService
  ) {
    this.appointmentForm = this.fb.group({
      doctorId: ['', Validators.required],
      patientId: ['', Validators.required],
      date: ['', Validators.required],
      hour: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      initialComplaint: ['', [Validators.required, Validators.maxLength(250)]],
    });

    const todayDate = new Date();
    const dd = String(todayDate.getDate()).padStart(2, '0');
    const mm = String(todayDate.getMonth() + 1).padStart(2, '0'); // January is 0!
    const yyyy = todayDate.getFullYear();

    this.today = `${yyyy}-${mm}-${dd}`;
  }

  ngOnInit(): void {
    this.loadDoctors();
    this.loadPatients();

    if (this.isEditMode && this.id) {
      this.loadAppointmentData(this.id);
    }

    this.appointmentForm.get('date')?.valueChanges.subscribe((selectedDate) => {
      const day = this.getDayFromDate(selectedDate);
      this.selectedDay = day;
      this.loadScheduleByDay(
        this.appointmentForm.get('doctorId')?.value,
        this.selectedDay
      );
    });

    this.appointmentForm.get('hour')?.valueChanges.subscribe((selectedHour) => {
      this.updateStartEndTime(selectedHour);
    });
  }

  loadPatients(): void {
    this.patientService.getPatients().subscribe({
      next: (response) => {
        this.patients = response.content;
      },
      error: (err) => {
        this.toastr.error('Failed to load patients');
      },
    });
  }

  loadDoctors(): void {
    this.doctorService.getDoctors().subscribe({
      next: (doctors) => {
        this.doctors = doctors;
      },
      error: (err) => {
        this.toastr.error('Failed to load doctors');
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

  onDoctorChange(): void {
    const selectedDoctorId = this.appointmentForm.get('doctorId')?.value;
    const selectedDate = this.appointmentForm.get('date')?.value;

    if (selectedDoctorId && selectedDate) {
      this.loadScheduleByDay(
        selectedDoctorId,
        this.getDayFromDate(selectedDate)
      );
    }
  }

  onDateChange(): void {
    const selectedPatientId = this.appointmentForm.get('patientId')?.value;
    const selectedDoctorId = this.appointmentForm.get('doctorId')?.value;
    const selectedDate = this.appointmentForm.get('date')?.value;

    if (selectedPatientId && selectedDoctorId && selectedDate) {
      this.loadScheduleByDay(
        selectedDoctorId,
        this.getDayFromDate(selectedDate)
      );
    }
  }

  loadScheduleByDay(doctorId: string | null, day: string): void {
    if (!doctorId) {
      console.error('Doctor ID is null, cannot load schedule.');
      return; // Exit if doctorId is null
    }

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

  loadAppointmentData(appointmentId: string): void {
    this.appointmentService.getAppointmentById(appointmentId).subscribe({
      next: (appointmentData) => {
        this.appointmentForm.patchValue({
          doctorId: appointmentData.doctorId,
          patientId: appointmentData.patientId,
          date: appointmentData.date,
          startTime: appointmentData.startTime,
          endTime: appointmentData.endTime,
          initialComplaint: appointmentData.initialComplaint,
        });

        this.loadScheduleByDay(appointmentData.doctorId, this.getDayFromDate(appointmentData.date));
      },
      error: (err) => {
        this.toastr.error('Failed to load appointment data');
      },
    });
  }

  submitForm(): boolean {
    if (this.isSubmitting || this.appointmentForm.invalid) {
      return false;
    }

    const formData: AppointmentSaveDTO = {
      doctorId: this.appointmentForm.value.doctorId,
      patientId: this.appointmentForm.value.patientId,
      initialComplaint: this.appointmentForm.value.initialComplaint,
      date: this.appointmentForm.value.date,
      startTime: this.appointmentForm.value.startTime,
      endTime: this.appointmentForm.value.endTime,
    };

    this.isSubmitting = true; // Set the flag to true to indicate submission

    if (this.isEditMode && this.id) {
      // Update appointment logic
      this.appointmentService.updateAppointment(this.id, formData).subscribe({
        next: () => {
          this.toastr.success('Appointment updated successfully!');
          this.isSubmitting = false; // Reset the flag
          this.router.navigate(['/admin/dashboard/appointments']);
        },
        error: (error: any) => {
          this.toastr.error('Failed to update appointment');
          this.isSubmitting = false; // Reset the flag
        },
      });
    } else {
      // Create appointment logic
      this.appointmentService.createAppointment(formData).subscribe({
        next: () => {
          this.toastr.success('Appointment created successfully!');
          this.isSubmitting = false; // Reset the flag
          this.router.navigate(['/admin/dashboard/appointments']);
        },
        error: (error: any) => {
          this.toastr.error(
            'You already have an appointment with this doctor at the same time'
          );
          console.error('Error:', error);
          this.isSubmitting = false; // Reset the flag
        },
      });
    }

    return true;
  }
}
