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
import { ActivatedRoute } from '@angular/router';

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
import { DoctorSchedulesService } from '../../../../services/doctor-schedule-service/doctor-schedules.service';
import { DoctorScheduleList } from '../../../../models/doctor-schedule.model';

@Component({
  selector: 'app-appointment-search',
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
  templateUrl: './appointment-search.component.html',
  styleUrls: ['./appointment-search.component.css'], // Note: Corrected styleUrl to styleUrls
})
export class AppointmentSearchComponent implements OnInit {
  doctorName: string | '' = '';
  date: string | '' = '';
  schedules: DoctorScheduleList[] = [];
  groupedSchedules: { [key: string]: DoctorScheduleList[] } = {};
  isLoading = true; // Add a loading flag
  hasNoData = false; // Add a no-data flag

  constructor(
    private route: ActivatedRoute,
    private doctorSchedulesService: DoctorSchedulesService
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      this.doctorName = params['doctorName'] || null;
      this.date = params['date'] || null;

      if (this.doctorName || this.date) {
        this.loadSchedules();
      } else {
        this.isLoading = false;
      }
    });
  }

  loadSchedules() {
    this.isLoading = true; // Set loading to true when fetching data
    this.doctorSchedulesService
      .getFilteredSchedules(this.doctorName, this.date)
      .subscribe(
        (data: DoctorScheduleList[]) => {
          this.schedules = data;
          this.isLoading = false;

          if (data.length === 0) {
            this.hasNoData = true; // If no data, set the no-data flag
          } else {
            this.hasNoData = false; // Reset flag if data exists
            this.groupSchedulesByDoctor(); // Group schedules if there is data
          }
        },
        (error) => {
          console.error('Error fetching schedules:', error);
          this.isLoading = false;
          this.hasNoData = true; // Treat error as no data
        }
      );
  }

  groupSchedulesByDoctor() {
    this.groupedSchedules = this.schedules.reduce(
      (acc: { [key: string]: DoctorScheduleList[] }, schedule) => {
        const doctorId = schedule.doctor.id;
        if (!acc[doctorId]) {
          acc[doctorId] = [];
        }
        acc[doctorId].push(schedule);
        acc[doctorId] = this.sortSchedulesByDay(acc[doctorId]);
        return acc;
      },
      {} as { [key: string]: DoctorScheduleList[] }
    );
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
