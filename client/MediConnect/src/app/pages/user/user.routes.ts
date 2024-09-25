import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AppointmentCreateComponent } from './appointment/appointment-create/appointment-create.component';
import { AppointmentHistoryComponent } from './appointment/appointment-history/appointment-history.component';
import { AppointmentMyComponent } from './appointment/appointment-my/appointment-my.component';

export const userRoutes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'appointment/create', component: AppointmentCreateComponent },
  { path: 'appointment/history', component: AppointmentHistoryComponent },
  { path: 'my-appointment', component: AppointmentMyComponent },
];
