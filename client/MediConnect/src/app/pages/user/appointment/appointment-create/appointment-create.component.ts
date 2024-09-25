import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
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
import { DoctorSchedulesService } from '../../../../services/doctor-schedule-service/doctor-schedules.service';
import { DoctorScheduleList } from '../../../../models/doctor-schedule.model';

@Component({
  selector: 'app-appointment-create',
  standalone: true,
  imports: [
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
  ],
  templateUrl: './appointment-create.component.html',
  styleUrls: ['./appointment-create.component.css'],
})
export class AppointmentCreateComponent implements OnInit {
  doctorSchedules: DoctorScheduleList[] = [];
  groupedSchedules: { [key: string]: DoctorScheduleList[] } = {};

  constructor(private doctorScheduleService: DoctorSchedulesService) {}

  ngOnInit() {
    this.loadSchedules();
  }

  loadSchedules() {
    this.doctorScheduleService.getSchedulesDoctor().subscribe({
      next: (response) => {
        this.doctorSchedules = response;
        this.groupSchedulesByDoctor();
        console.log('Grouped schedules:', this.groupedSchedules);
      },
      error: (error) => {
        console.error('Error loading doctor schedules:', error);
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
      // Sort the schedules by day from Monday to Friday
      acc[doctorId] = this.sortSchedulesByDay(acc[doctorId]);
      return acc;
    }, {} as { [key: string]: DoctorScheduleList[] });
  }

  // Define the days in order from Monday to Friday
  dayOrder: string[] = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];

  // Helper function to sort days according to the specified order
  sortSchedulesByDay(schedules: any[]): any[] {
    return schedules.sort((a, b) => {
      return this.dayOrder.indexOf(a.day) - this.dayOrder.indexOf(b.day);
    });
  }
}
