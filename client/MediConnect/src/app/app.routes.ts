import { Routes } from '@angular/router';
import { RouterConfig } from './config/app.constants';
// import { authGuard } from './main/guards/auth-guard.service';

export const routes: Routes = [
  {
    path: RouterConfig.HOME.path,
    loadChildren: () =>
      import('./pages/home/home.routes').then((m) => m.homeRoutes),
    title: RouterConfig.SIGNIN.title,
    data: RouterConfig.SIGNIN.data,
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
    path: RouterConfig.USERDASHBOARD.path,
    loadChildren: () =>
      import('./pages/user/user.routes').then((m) => m.userRoutes),
    title: RouterConfig.USERDASHBOARD.title,
    data: RouterConfig.USERDASHBOARD.data,
  },
];
