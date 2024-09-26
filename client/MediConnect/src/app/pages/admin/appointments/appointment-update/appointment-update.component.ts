import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HlmButtonDirective } from '../../../../components/ui-button-helm/src/lib/hlm-button.directive';
import { AppointmentFormComponent } from '../appointment-form/appointment-form.component';

@Component({
  selector: 'app-appointment-update',
  standalone: true,
  imports: [HlmButtonDirective, AppointmentFormComponent],
  templateUrl: './appointment-update.component.html',
  styleUrl: './appointment-update.component.css',
})
export class AppointmentUpdateComponent {
  appointmentId: string | null = null; // Store the user ID from the route

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.appointmentId = params['id']; // Access the 'id' parameter from the URL
      console.log('Test ID:', this.appointmentId);
    });
  }
}
