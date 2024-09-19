import { Component, Input } from '@angular/core';
import { DoctorSchedule } from '../../../../models/doctor-schedule.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-doctor-schedule-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './doctor-schedule-modal.component.html',
  styleUrl: './doctor-schedule-modal.component.css'
})
export class DoctorScheduleModalComponent {
  @Input() doctorSchedule !: DoctorSchedule;

}
