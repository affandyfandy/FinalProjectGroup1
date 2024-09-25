import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AppointmentCreateComponent } from './appointment/appointment-create/appointment-create.component';
import { AppointmentHistoryComponent } from './appointment/appointment-history/appointment-history.component';
import { AppointmentMyComponent } from './appointment/appointment-my/appointment-my.component';
import { ProfileComponent } from './profile/profile.component';

export const userRoutes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'appointment', component: AppointmentCreateComponent },
  { path: 'history', component: AppointmentHistoryComponent },
  { path: 'profile', component: ProfileComponent },
];
