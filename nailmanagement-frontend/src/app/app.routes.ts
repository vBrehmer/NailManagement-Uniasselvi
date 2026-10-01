import { Routes } from '@angular/router';
import { AgendaComponent } from './pages/agenda/agenda.component';
import { ClienteComponent } from './pages/cliente/cliente.component';
import { ServicoComponent } from './pages/servico/servico.component';
import { AgenteComponent } from './pages/agente/agente.component';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from './core/auth/auth.guard';
import { guestGuard } from './core/guest/guest.guard';
import { RegisterComponent } from './pages/register/register.component';

export const routes: Routes = [

    {path: 'login', component: LoginComponent, canActivate: [guestGuard]},
    {path: 'registrar', component: RegisterComponent, canActivate: [guestGuard]},
    {path: 'agenda', component: AgendaComponent, canActivate: [authGuard]},
    {path: 'cliente', component: ClienteComponent, canActivate: [authGuard]},
    {path: 'servico', component: ServicoComponent, canActivate: [authGuard]},
    {path: 'profissional', component: AgenteComponent, canActivate: [authGuard]},
    { path: '', redirectTo: 'agenda', pathMatch: 'full' },
    { path: '**', redirectTo: 'agenda' }
];
