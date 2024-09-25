import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DoctorSchedulesService } from '../../../../services/doctor-schedule-service/doctor-schedules.service';
import { ToastrService } from 'ngx-toastr';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
@Component({
  selector: 'app-doctor-schedule-edit-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HlmInputDirective,
    HlmButtonDirective,
    HlmLabelDirective,
    HlmIconComponent,
    NgIconComponent,
  ],
  templateUrl: './doctor-schedule-edit-form.component.html',
  styleUrl: './doctor-schedule-edit-form.component.css'
})
export class DoctorScheduleEditFormComponent implements OnInit {
  doctorScheduleEditForm!: FormGroup;
  scheduleId!: string;
  startWorkingHour!: string;

  constructor(
    private fb: FormBuilder,
    private doctorScheduleService: DoctorSchedulesService,
    private route: ActivatedRoute,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.scheduleId = this.route.snapshot.paramMap.get('id')!;
    this.startWorkingHour = this.route.snapshot.paramMap.get('startWorkingHour')!;

    this.initForm();
    this.loadScheduleData();
  }

  initForm(): void {
    this.doctorScheduleEditForm = this.fb.group({
      startWorkingHour: [{ value: '', disabled: true }, Validators.required],
      endWorkingHour: ['', Validators.required],
      maxPatient: ['', [Validators.required, Validators.min(1)]],
    });
  }

  loadScheduleData(): void {
    this.doctorScheduleService
      .getScheduleTime(this.scheduleId, this.startWorkingHour)
      .subscribe((scheduleTime) => {
        this.doctorScheduleEditForm.patchValue({
          startWorkingHour: scheduleTime.startWorkingHour,
          endWorkingHour: scheduleTime.endWorkingHour,
          maxPatient: scheduleTime.maxPatient,
        });
      }, error => {
        this.toastr.error('Failed to load schedule data');
      });
  }

  onSubmit(): void {
    if (this.doctorScheduleEditForm.valid) {
      const updatedSchedule = this.doctorScheduleEditForm.value;

      this.doctorScheduleService
        .updateScheduleTime(this.scheduleId, this.startWorkingHour, updatedSchedule)
        .subscribe(
          () => {
            this.toastr.success('Schedule updated successfully!');
            this.router.navigate(['/admin/dashboard/schedules']);
          },
          (error) => {
            this.toastr.error('Failed to update schedule');
          }
        );
    } else {
      this.toastr.error('Form is invalid');
    }
  }

  get doctorName() {
    return this.doctorScheduleEditForm.get('doctorName');
  }

  get day() {
    return this.doctorScheduleEditForm.get('day');
  }

  get endWorkingHour() {
    return this.doctorScheduleEditForm.get('endWorkingHour');
  }

  get maxPatient() {
    return this.doctorScheduleEditForm.get('maxPatient');
  }
}

