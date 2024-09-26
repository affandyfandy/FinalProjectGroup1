import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, ParamMap } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { DayFilterPipe } from '../../../../core/pipes/dayfilter.pipe';
import { BrnMenuTriggerDirective } from '@spartan-ng/ui-menu-brain';
import {
  HlmMenuComponent,
  HlmMenuGroupComponent,
  HlmMenuItemDirective,
  HlmMenuItemIconDirective,
  HlmMenuItemSubIndicatorComponent,
  HlmMenuLabelComponent,
  HlmMenuSeparatorComponent,
  HlmMenuShortcutComponent,
  HlmSubMenuComponent,
} from '@spartan-ng/ui-menu-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import {
  BrnDialogContentDirective,
  BrnDialogTriggerDirective,
} from '@spartan-ng/ui-dialog-brain';
import {
  HlmDialogComponent,
  HlmDialogContentComponent,
  HlmDialogDescriptionDirective,
  HlmDialogFooterComponent,
  HlmDialogHeaderComponent,
  HlmDialogTitleDirective,
} from '@spartan-ng/ui-dialog-helm';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { FormsModule } from '@angular/forms';
import { DoctorScheduleList } from '../../../../models/doctor-schedule.model';
import { DoctorSchedulesService } from '../../../../services/doctor-schedule-service/doctor-schedules.service';
import {
  Specializations,
  SpecializationData,
} from '../../../../utils/clinic_specializations';

import {
  HlmPaginationContentDirective,
  HlmPaginationDirective,
  HlmPaginationEllipsisComponent,
  HlmPaginationItemDirective,
  HlmPaginationLinkDirective,
  HlmPaginationNextComponent,
  HlmPaginationPreviousComponent,
} from '@spartan-ng/ui-pagination-helm';
import { AppointmentFormComponent } from '../appointment-form/appointment-form.component';
import { AppointmentSpecializationSymptompsComponent } from './appointment-specialization-symptomps/appointment-specialization-symptomps.component';
import { AppointmentSpecializationPreventComponent } from './appointment-specialization-prevent/appointment-specialization-prevent.component';

@Component({
  standalone: true,
  imports: [
    AppointmentSpecializationSymptompsComponent,
    AppointmentSpecializationPreventComponent,
    AppointmentFormComponent,
    CommonModule,
    RouterModule,
    BrnMenuTriggerDirective,
    HlmMenuComponent,
    HlmSubMenuComponent,
    HlmMenuItemDirective,
    HlmMenuItemSubIndicatorComponent,
    HlmMenuLabelComponent,
    HlmMenuShortcutComponent,
    HlmMenuSeparatorComponent,
    HlmMenuItemIconDirective,
    HlmMenuGroupComponent,
    BrnDialogTriggerDirective,
    BrnDialogContentDirective,
    HlmDialogComponent,
    HlmDialogContentComponent,
    HlmDialogHeaderComponent,
    HlmDialogFooterComponent,
    HlmDialogTitleDirective,
    HlmDialogDescriptionDirective,
    HlmLabelDirective,
    HlmInputDirective,
    HlmButtonDirective,
    DayFilterPipe,
    FormsModule,
    HlmPaginationDirective,
    HlmPaginationContentDirective,
    HlmPaginationItemDirective,
    HlmPaginationPreviousComponent,
    HlmPaginationNextComponent,
    HlmPaginationLinkDirective,
    HlmPaginationEllipsisComponent,
  ],
  selector: 'app-appointment-specialization',
  templateUrl: './appointment-specialization.component.html',
  styleUrls: ['./appointment-specialization.component.css'],
})
export class AppointmentSpecializationComponent implements OnInit {
  specialization: string = '';
  doctorSchedules: DoctorScheduleList[] = [];
  groupedSchedules: { [key: string]: DoctorScheduleList[] } = {};
  specializationData: SpecializationData | null = null;
  constructor(
    private route: ActivatedRoute,
    private doctorScheduleService: DoctorSchedulesService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      let specialization = params.get('specialization') || '';
      specialization = decodeURIComponent(specialization);
      this.specialization = specialization;
      this.loadSpecializationData(); // Change to load from imported data
      this.loadSchedules();
    });
  }

  loadSpecializationData() {
    this.specializationData =
      Specializations.find(
        (spec) => spec.name.toLowerCase() === this.specialization.toLowerCase()
      ) || null;
  }

  loadSchedules() {
    if (this.specialization) {
      this.doctorScheduleService
        .getFilteredSchedules(undefined, undefined, this.specialization)
        .subscribe({
          next: (schedules: DoctorScheduleList[]) => {
            this.doctorSchedules = schedules;
            this.groupSchedulesByDoctor();
          },
          error: (error) => {
            console.error('Error fetching schedules:', error);
          },
        });
    }
  }

  groupSchedulesByDoctor() {
    this.groupedSchedules = this.doctorSchedules.reduce((acc, schedule) => {
      const doctorId = schedule.doctor.id;
      if (!acc[doctorId]) {
        acc[doctorId] = [];
      }
      acc[doctorId].push(schedule);
      acc[doctorId] = this.sortSchedulesByDay(acc[doctorId]);
      return acc;
    }, {} as { [key: string]: DoctorScheduleList[] });
  }

  objectKeys(obj: any): string[] {
    return Object.keys(obj);
  }

  dayOrder: string[] = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];

  sortSchedulesByDay(schedules: any[]): any[] {
    return schedules.sort((a, b) => {
      return this.dayOrder.indexOf(a.day) - this.dayOrder.indexOf(b.day);
    });
  }
}
