import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { inject } from '@angular/core';

export const guestGuard: CanActivateFn = (route, state) => {
  const authService = inject(UserService);
  const router = inject(Router);

  if (authService.estaLogado()) {
    router.navigate(['/login']); 
    return false;
  }

  return true;
};
