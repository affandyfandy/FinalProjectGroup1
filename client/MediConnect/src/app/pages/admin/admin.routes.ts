import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserListComponent } from './users/user-list/user-list.component';
import { PatientListComponent } from './patients/patient-list/patient-list.component';
import { PatientsUpdateComponent } from './patients/patients-update/patients-update.component';
import { PatientsCreateComponent } from './patients/patients-create/patients-create.component';
import { UserCreateComponent } from './users/user-create/user-create.component';
import { UserUpdateComponent } from './users/user-update/user-update.component';
import { DoctorListComponent } from './doctors/doctor-list/doctor-list.component';
import { DoctorUpdateComponent } from './doctors/doctor-update/doctor-update.component';
import { DoctorCreateComponent } from './doctors/doctor-create/doctor-create.component';
import { DoctorScheduleListComponent } from './doctor-schedules/doctor-schedule-list/doctor-schedule-list.component';

export const adminRoutes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'users', component: UserListComponent },
  { path: 'users/create', component: UserCreateComponent },
  { path: 'users/:id', component: UserUpdateComponent },
  { path: 'patients', component: PatientListComponent },
  { path: 'patients/create', component: PatientsCreateComponent },
  { path: 'patients/:id', component: PatientsUpdateComponent },
  { path: 'doctors', component: DoctorListComponent },
  { path: 'doctors/create', component: DoctorCreateComponent },
  { path: 'doctors/:id', component: DoctorUpdateComponent },
  { path: 'schedules', component: DoctorScheduleListComponent },
];
