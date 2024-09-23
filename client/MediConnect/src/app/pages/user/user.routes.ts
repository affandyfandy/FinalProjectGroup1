import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';

export const userRoutes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'appointment/create', component: DashboardComponent },
  { path: 'appointment/history', component: DashboardComponent },
];
