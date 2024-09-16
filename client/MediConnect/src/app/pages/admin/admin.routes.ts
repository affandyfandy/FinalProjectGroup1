import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserListComponent } from './users/user-list/user-list.component';
import { PatientListComponent } from './patients/patient-list/patient-list.component';
import { PatientsUpdateComponent } from './patients/patients-update/patients-update.component';
import { PatientsCreateComponent } from './patients/patients-create/patients-create.component';
import { UserCreateComponent } from './users/user-create/user-create.component';
import { UserUpdateComponent } from './users/user-update/user-update.component';

export const adminRoutes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'users', component: UserListComponent },
  { path: 'users/create', component: UserCreateComponent },
  { path: 'users/:id', component: UserUpdateComponent },
  { path: 'patients', component: PatientListComponent },
  { path: 'patients/create', component: PatientsCreateComponent },
  { path: 'patients/:id', component: PatientsUpdateComponent },
];
