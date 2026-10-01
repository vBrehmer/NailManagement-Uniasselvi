import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../services/user.service';

export const authGuard: CanActivateFn = () => {

  const authService = inject(UserService);
  const router = inject(Router);

  if (authService.estaLogado()) {
    return true;
  }

  return router.parseUrl('/login'); 

};
