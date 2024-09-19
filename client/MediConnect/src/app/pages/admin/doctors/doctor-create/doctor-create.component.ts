import { Component } from '@angular/core';
import { DoctorFormComponent } from '../doctor-form/doctor-form.component';

@Component({
  selector: 'app-doctor-create',
  standalone: true,
  imports: [DoctorFormComponent],
  templateUrl: './doctor-create.component.html',
  styleUrl: './doctor-create.component.css'
})
export class DoctorCreateComponent {

}
