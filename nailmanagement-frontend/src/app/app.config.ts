import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideNativeDateAdapter } from '@angular/material/core';
import { AutenticacaoInterceptor } from './core/interceptors/autenticacao.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
      withInterceptors([AutenticacaoInterceptor])
    ),
    provideRouter(routes),
    provideNativeDateAdapter()
  ,
    provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes),
    provideHttpClient(),
    provideNativeDateAdapter(),
    
  ]
};
