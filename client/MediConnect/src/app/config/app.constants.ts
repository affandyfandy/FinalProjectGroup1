export const AppConstants = {
  APPLICATION_NAME: 'MediConnect',
  APPLICATION_ICON: 'icon',
  BASE_API_URL: 'http://localhost:8080/api/v1',
  LOG_OFF_ICON: 'sign-out',
};

export interface RouteLink {
  path: string;
  link: string;
}

export const RouterConfig = {
  HOME: {
    path: '',
    link: '/',
    title: 'MediConnect',
  },
  Dashboard: { path: 'dashboard', link: '/dashboard', title: 'Dashboard' },
  SIGNIN: {
    path: 'signin',
    link: '/signin',
    title: 'Signin',
    data: { header: true },
  },
  SIGNUP: {
    path: 'signup',
    link: '/signup',
    title: 'Signup',
    data: { header: true },
  },
  USERDASHBOARD: {
    path: 'dashboard',
    link: '/dashboard',
    title: 'Dashboard',
    data: { header: true },
  },
  ADMINDASHBOARD: {
    path: 'admin/dashboard',
    link: '/admin/dashboard',
    title: 'Admin Dashboard',
    data: { header: true },
  }
};
