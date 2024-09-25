import { Component } from '@angular/core';
import { DoctorScheduleFormComponent } from "../doctor-schedule-form/doctor-schedule-form.component";

@Component({
  selector: 'app-doctor-schedule-create',
  standalone: true,
  imports: [DoctorScheduleFormComponent],
  templateUrl: './doctor-schedule-create.component.html',
  styleUrl: './doctor-schedule-create.component.css'
})
export class DoctorScheduleCreateComponent {

}
