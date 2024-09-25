import { Routes } from '@angular/router';
import { RouterConfig } from './config/app.constants';
import { authGuard } from './main/guards/auth.guard';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { UnauthorizeComponent } from './pages/unauthorize/unauthorize.component';
// import { authGuard } from './main/guards/auth-guard.service';

export const routes: Routes = [
  {
    path: RouterConfig.HOME.path,
    loadChildren: () =>
      import('./pages/home/home.routes').then((m) => m.homeRoutes),
    title: RouterConfig.HOME.title,
  },
  {
    path: RouterConfig.SIGNUP.path,
    loadChildren: () =>
      import('./pages/auth/signup/signup.routes').then((m) => m.signupRoutes),
    title: RouterConfig.SIGNUP.title,
    data: RouterConfig.SIGNUP.data,
  },
  {
    path: RouterConfig.SIGNIN.path,
    loadChildren: () =>
      import('./pages/auth/signin/signin.routes').then((m) => m.signinRoutes),
    title: RouterConfig.SIGNIN.title,
    data: RouterConfig.SIGNIN.data,
  },
  {
    path: '',
    canActivate: [authGuard],
    data: { roles: ['PATIENT'] },
    children: [
      {
        path: RouterConfig.USERDASHBOARD.path,
        loadChildren: () =>
          import('./pages/user/user.routes').then((m) => m.userRoutes),
        title: RouterConfig.USERDASHBOARD.title,
        data: RouterConfig.USERDASHBOARD.data,
      },
    ],
  },
  {
    path: '',
    canActivate: [authGuard],
    data: { roles: ['ADMIN', 'SUPERADMIN'] },
    children: [
      {
        path: RouterConfig.ADMINDASHBOARD.path,
        loadChildren: () =>
          import('./pages/admin/admin.routes').then((m) => m.adminRoutes),
        title: RouterConfig.ADMINDASHBOARD.title,
        data: RouterConfig.ADMINDASHBOARD.data,
      },
    ],
  },
  { path: 'unauthorized', component: UnauthorizeComponent },
  { path: '404', component: NotFoundComponent },
  { path: '**', component: NotFoundComponent },
];
