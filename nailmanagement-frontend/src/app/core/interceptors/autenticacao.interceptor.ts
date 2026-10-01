import { HttpInterceptorFn } from '@angular/common/http';
import { TokenService } from '../services/token.service';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const AutenticacaoInterceptor: HttpInterceptorFn = (req, next) => {
   const token = inject(TokenService).retornarToken();
   const router = inject(Router);
   if (token) {
    req = req.clone ({
      setHeaders: {
        'Authorization': `Bearer ${token}`
      }
    });
 }
    return next(req);
};
