import { Component } from '@angular/core';
import { AppointmentFormComponent } from '../appointment-form/appointment-form.component';

@Component({
  selector: 'app-appointment-create',
  standalone: true,
  imports: [AppointmentFormComponent],
  templateUrl: './appointment-create.component.html',
  styleUrl: './appointment-create.component.css',
})
export class AppointmentCreateComponent {}
