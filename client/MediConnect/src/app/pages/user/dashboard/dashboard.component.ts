import { Component } from '@angular/core';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { RouterModule } from '@angular/router';
import { AppointmentListComponent } from './appointment-list/appointment-list.component';
import { QueueListComponent } from './queue-list/queue-list.component';
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    RouterModule,
    HlmButtonDirective,
    AppointmentListComponent,
    QueueListComponent,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {}
