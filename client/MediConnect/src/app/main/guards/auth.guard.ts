import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../services/auth-service/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Get the required roles from the route's data
  const requiredRoles = route.data['roles'];

  // Check if the user is logged in
  if (authService.isLoggedIn()) {
    const userRole = authService.getRole(); // Assume getUserRole() returns the user's role

    // Check if the user role matches any of the required roles
    if (requiredRoles.includes(userRole)) {
      return true;
    } else {
      // Redirect to unauthorized page or handle access denial
      router.navigate(['/unauthorized']);
      return false;
    }
  }

  // If not logged in, redirect to the signin page
  router.navigate(['/signin']);
  return false;
};
