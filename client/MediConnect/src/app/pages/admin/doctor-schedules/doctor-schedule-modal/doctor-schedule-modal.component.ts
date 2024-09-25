import { Component, Input, Output, EventEmitter } from '@angular/core';
import { DoctorSchedule, ListDoctorSchedule } from '../../../../models/doctor-schedule.model';
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
  @Input() listDoctorSchedule !: ListDoctorSchedule;

  @Output() onUpdateSchedule = new EventEmitter<any>();

  updatedData = {
    startWorkingHour: '',
    endWorkingHour: '',
    maxPatient: 0,
  };

  ngOnInit() {
    this.updatedData.startWorkingHour = this.listDoctorSchedule.startWorkingHour;
    this.updatedData.endWorkingHour = this.listDoctorSchedule.endWorkingHour;
    this.updatedData.maxPatient = this.listDoctorSchedule.maxPatient;
  }

  updateSchedule() {
    this.onUpdateSchedule.emit(this.updatedData);
  }
}
