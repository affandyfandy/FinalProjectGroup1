import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
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
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { DoctorScheduleList } from '../../../../models/doctor-schedule.model';
import { DoctorSchedulesService } from '../../../../services/doctor-schedule-service/doctor-schedules.service';

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

@Component({
  standalone: true,
  imports: [
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
    DayFilterPipe, // Register the pipe as standalone
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

  constructor(
    private route: ActivatedRoute,
    private doctorScheduleService: DoctorSchedulesService
  ) {}

  ngOnInit(): void {
    // Get specialization from either path or query parameters
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.specialization = params.get('specialization') || '';
      if (!this.specialization) {
        this.route.queryParamMap.subscribe((queryParams: ParamMap) => {
          this.specialization = queryParams.get('specialization') || '';
          this.loadSchedules();
        });
      } else {
        this.loadSchedules();
      }
    });
  }

  // Method to load schedules based on specialization
  loadSchedules() {
    if (this.specialization) {
      console.log('Loading schedules for:', this.specialization);
      this.doctorScheduleService
        .getFilteredSchedules(undefined, undefined, this.specialization)
        .subscribe({
          next: (schedules: DoctorScheduleList[]) => {
            this.doctorSchedules = schedules;
            this.groupSchedulesByDoctor();
            console.log('Received schedules:', schedules);
          },
          error: (error) => {
            console.error('Error fetching schedules:', error);
          },
        });
    } else {
      console.log('No specialization provided');
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

  // Helper function to return object keys (doctor IDs)
  objectKeys(obj: any): string[] {
    return Object.keys(obj);
  }

  // Sort schedules by day
  dayOrder: string[] = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];

  sortSchedulesByDay(schedules: any[]): any[] {
    return schedules.sort((a, b) => {
      return this.dayOrder.indexOf(a.day) - this.dayOrder.indexOf(b.day);
    });
  }
}
