import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AppointmentCreateComponent } from './appointment/appointment-create/appointment-create.component';
import { AppointmentHistoryComponent } from './appointment/appointment-history/appointment-history.component';
import { ProfileComponent } from './profile/profile.component';
import { AppointmentSearchComponent } from './appointment/appointment-search/appointment-search.component';
import { AppointmentSpecializationComponent } from './appointment/appointment-specialization/appointment-specialization.component';

export const userRoutes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'appointment', component: AppointmentCreateComponent },
  { path: 'appointment/search', component: AppointmentSearchComponent },
  {
    path: 'appointment/specialization/:specialization',
    component: AppointmentSpecializationComponent,
  },
  { path: 'history', component: AppointmentHistoryComponent },
  { path: 'profile', component: ProfileComponent },
];
