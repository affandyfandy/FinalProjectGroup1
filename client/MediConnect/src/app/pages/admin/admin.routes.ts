import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserListComponent } from './users/user-list/user-list.component';
import { PatientListComponent } from './patients/patient-list/patient-list.component';
import { PatientsUpdateComponent } from './patients/patients-update/patients-update.component';

export const adminRoutes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'users', component: UserListComponent },
  { path: 'patients', component: PatientListComponent },
  { path: 'patients/:id', component: PatientsUpdateComponent },
];
