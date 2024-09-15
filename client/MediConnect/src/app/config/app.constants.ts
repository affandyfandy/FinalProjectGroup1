export const AppConstants = {
  APPLICATION_NAME: 'MediConnect',
  APPLICATION_ICON: 'icon',
  BASE_API_URL: '/my-api',
  LOG_OFF_ICON: 'sign-out',
};

export interface RouteLink {
  path: string;
  link: string;
}

export const RouterConfig = {
  HOME: { path: '', link: '/', title: 'Home' },
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
    title: 'dashboard',
    data: { header: true },
  },
};
