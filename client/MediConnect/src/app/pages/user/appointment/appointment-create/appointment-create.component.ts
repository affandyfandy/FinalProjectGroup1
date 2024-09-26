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
  specializations: string[] = [];
  selectedSpecializations: { [key: string]: boolean } = {};
  isLoading: boolean = true;
  noData: boolean = false;
  isDropdownOpen: boolean = false; 

  constructor(
    private doctorScheduleService: DoctorSchedulesService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadSchedules();
  }

  loadSchedules() {
    this.doctorScheduleService.getSchedulesDoctor().subscribe({
      next: (response) => {
        this.doctorSchedules = response;
        this.groupSchedulesByDoctor();
        this.filteredSchedules = { ...this.groupedSchedules };
        this.extractSpecializations();
        this.noData = Object.keys(this.filteredSchedules).length === 0;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading doctor schedules:', error);
        this.isLoading = false;
      },
    });
  }

  // Toggle the dropdown open/close state
  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  groupSchedulesByDoctor() {
    this.groupedSchedules = this.doctorSchedules.reduce((acc, schedule) => {
      const doctorId = schedule.doctor.id;
      if (!acc[doctorId]) {
        acc[doctorId] = [];
      }
      acc[doctorId].push(schedule);
      return acc;
    }, {} as { [key: string]: DoctorScheduleList[] });
  }

  // Extract unique specializations from the doctor list
  extractSpecializations() {
    const specializationsSet = new Set<string>();
    this.doctorSchedules.forEach((schedule) => {
      specializationsSet.add(schedule.doctor.specialization);
    });
    this.specializations = Array.from(specializationsSet);
  }

  // Filter schedules based on the selected specializations and the doctor name
  filterSchedules() {
    const searchTerm = this.doctorName.toLowerCase();
    this.filteredSchedules = Object.keys(this.groupedSchedules)
      .filter((doctorId) => {
        const doctor = this.groupedSchedules[doctorId][0].doctor;
        const matchesName = doctor.name.toLowerCase().includes(searchTerm);
        const matchesSpecialization =
          this.selectedSpecializations[doctor.specialization] ||
          this.noSpecializationFilter();
        return matchesName && matchesSpecialization;
      })
      .reduce((acc, doctorId) => {
        acc[doctorId] = this.groupedSchedules[doctorId];
        return acc;
      }, {} as { [key: string]: DoctorScheduleList[] });
    this.noData = Object.keys(this.filteredSchedules).length === 0;
  }

  // Handle changes in the specialization filter
  onFilterSpecializationChange() {
    this.filterSchedules();
  }

  // Helper function to check if no specializations are selected (show all if no filter)
  noSpecializationFilter(): boolean {
    return !Object.values(this.selectedSpecializations).includes(true);
  }

  onSearch() {
    this.filterSchedules();
  }
}
