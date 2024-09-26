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
import { AppointmentFormComponent } from '../appointment-form/appointment-form.component';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DoctorSchedulesService } from '../../../../services/doctor-schedule-service/doctor-schedules.service';
import { DoctorScheduleList } from '../../../../models/doctor-schedule.model';

import {
  HlmPaginationContentDirective,
  HlmPaginationDirective,
  HlmPaginationEllipsisComponent,
  HlmPaginationItemDirective,
  HlmPaginationLinkDirective,
  HlmPaginationNextComponent,
  HlmPaginationPreviousComponent,
} from '@spartan-ng/ui-pagination-helm';

@Component({
  selector: 'app-appointment-create',
  standalone: true,
  imports: [
    AppointmentFormComponent,
    CommonModule,
    RouterModule,
    AppointmentFormComponent,
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
  templateUrl: './appointment-create.component.html',
  styleUrls: ['./appointment-create.component.css'],
})
export class AppointmentCreateComponent implements OnInit {
  doctorSchedules: DoctorScheduleList[] = [];
  groupedSchedules: { [key: string]: DoctorScheduleList[] } = {};
  filteredSchedules: { [key: string]: DoctorScheduleList[] } = {};
  doctorName: string = '';
  isLoading: boolean = true;
  noData: boolean = false;

  constructor(
    private doctorScheduleService: DoctorSchedulesService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadSchedules();
  }

  onSearch() {
    if (this.doctorName) {
      this.filterSchedules(); // Call filter function on search
    } else {
      this.filteredSchedules = { ...this.groupedSchedules }; // Reset filtered schedules if input is empty
    }
  }

  loadSchedules() {
    this.doctorScheduleService.getSchedulesDoctor().subscribe({
      next: (response) => {
        this.doctorSchedules = response;
        this.groupSchedulesByDoctor();
        this.filteredSchedules = { ...this.groupedSchedules };
        this.noData = Object.keys(this.filteredSchedules).length === 0; // Check if there are no schedules
        this.isLoading = false; // Set loading to false after fetching
        console.log('Grouped schedules:', this.groupedSchedules);
      },
      error: (error) => {
        console.error('Error loading doctor schedules:', error);
        this.isLoading = false;
      },
    });
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

  filterSchedules() {
    const searchTerm = this.doctorName.toLowerCase(); // Convert to lowercase for case insensitive search
    this.filteredSchedules = Object.keys(this.groupedSchedules)
      .filter((doctorId) => {
        const doctorName =
          this.groupedSchedules[doctorId][0].doctor.name.toLowerCase();
        return doctorName.includes(searchTerm); // Check if the doctor's name includes the search term
      })
      .reduce((acc, doctorId) => {
        acc[doctorId] = this.groupedSchedules[doctorId]; // Keep only the filtered doctors
        return acc;
      }, {} as { [key: string]: DoctorScheduleList[] });
    this.noData = Object.keys(this.filteredSchedules).length === 0;
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
