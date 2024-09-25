import { Component, Input, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
  FormArray,
} from '@angular/forms';
import { bootstrapPlusCircle } from '@ng-icons/bootstrap-icons';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { DoctorSchedulesService } from '../../../../services/doctor-schedule-service/doctor-schedules.service';
import { DoctorsService } from '../../../../services/doctor-service/doctors.service';

@Component({
  selector: 'app-doctor-schedule-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HlmInputDirective,
    HlmButtonDirective,
    HlmLabelDirective,
    HlmIconComponent,
    NgIconComponent,
    RouterModule,
  ],
  viewProviders: [
    provideIcons({
      bootstrapPlusCircle
    }),
  ],
  templateUrl: './doctor-schedule-form.component.html',
  styleUrl: './doctor-schedule-form.component.css'
})
export class DoctorScheduleFormComponent implements OnInit {
  @Input() doctorId: string | null = null;

  doctorScheduleForm!: FormGroup;
  doctors:any[] = [];

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private doctorScheduleService: DoctorSchedulesService,
    private doctorService: DoctorsService,
    private toastr: ToastrService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadDoctors();
  }

  initForm(): void {
    this.doctorScheduleForm = this.fb.group({
      doctorId: ['', Validators.required],
      schedules: this.fb.array([this.createScheduleFormGroup()]),
    });
  }

  loadDoctors(): void {
    this.doctorService.getDoctors().subscribe({
      next: (doctors) => {
        this.doctors = doctors;
      },
      error: (err) => {
        this.toastr.error('Failed to load doctors');
      }
    })
  }

  get schedules(): FormArray {
    return this.doctorScheduleForm.get('schedules') as FormArray;
  }

  getScheduleTimes(scheduleIndex: number): FormArray {
    return (this.schedules.at(scheduleIndex).get('scheduleTimes') as FormArray);
  }

  createScheduleFormGroup(): FormGroup {
    return this.fb.group({
      day: ['', Validators.required],
      scheduleTimes: this.fb.array([this.createScheduleTimeFormGroup()]),
    });
  }

  createScheduleTimeFormGroup(): FormGroup {
    return this.fb.group({
      startWorkingHour: ['', Validators.required],
      endWorkingHour: ['', Validators.required],
      maxPatient: ['', [Validators.required, Validators.min(1)]],
    });
  }

  addSchedule(): void {
    this.schedules.push(this.createScheduleFormGroup());
  }

  removeSchedule(index: number): void {
    if (this.schedules.length > 1) {
      this.schedules.removeAt(index);
    }
  }

  addScheduleTime(scheduleIndex: number): void {
    const schedule = this.schedules.at(scheduleIndex) as FormGroup;
    const scheduleTimes = schedule.get('scheduleTimes') as FormArray;
    scheduleTimes.push(this.createScheduleTimeFormGroup());
  }

  removeScheduleTime(scheduleIndex: number, timeIndex: number): void {
    const schedule = this.schedules.at(scheduleIndex) as FormGroup;
    const scheduleTimes = schedule.get('scheduleTimes') as FormArray;
    if (scheduleTimes.length > 1) {
      scheduleTimes.removeAt(timeIndex);
    }
  }

  onSubmit(): void {
    if (this.doctorScheduleForm.valid) {
      const formData = this.doctorScheduleForm.value;

      const schedules = formData.schedules.map((schedule: any) => ({
        doctorId: formData.doctorId,
        day: schedule.day,
        scheduleTimes: schedule.scheduleTimes
      }));

      this.doctorScheduleService.createSchedule(schedules).subscribe({
        next: () => {
          this.toastr.success('Schedule created successfully!');
          this.router.navigate(['/admin/dashboard/schedules']);
        },
        error: (error: any) => {
          this.handleServerError(error);
        }
      });
    } else {
      this.toastr.error('Form is invalid');
      this.doctorScheduleForm.markAllAsTouched();
    }
  }

  handleServerError(error: any): void {
    console.error('Server error:', error);
    if (error.status === 400) {
      const errorMessage = error?.error || 'An unknown error occurred';
      this.toastr.error(errorMessage);
    } else {
      this.toastr.error('Failed to create schedule.');
    }
  }


  get doctorIdControl() {
    return this.doctorScheduleForm.get('doctorId');
  }
}
