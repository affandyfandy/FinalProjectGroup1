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
  LOGIN: {
    path: 'login',
    link: '/login',
    title: 'Login',
    data: { header: true },
  },
};
