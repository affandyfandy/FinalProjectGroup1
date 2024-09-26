import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { DoctorScheduleList } from '../../../../models/doctor-schedule.model';
import { DoctorSchedulesService } from '../../../../services/doctor-schedule-service/doctor-schedules.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [CommonModule],
  selector: 'app-appointment-specialization',
  templateUrl: './appointment-specialization.component.html',
  styleUrls: ['./appointment-specialization.component.css'],
})
export class AppointmentSpecializationComponent implements OnInit {
  specialization: string = '';
  doctorSchedules: DoctorScheduleList[] = [];

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
}
