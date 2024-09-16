import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserListComponent } from './users/user-list/user-list.component';

export const adminRoutes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'users', component: UserListComponent },
];
