import { inject, Injectable } from '@angular/core';
import { TokenService } from './token.service';
import { jwtDecode } from 'jwt-decode'
import { BehaviorSubject } from 'rxjs';
import { Cliente, RetornoUser } from '../types/type';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userSubject = new BehaviorSubject<Cliente | null>(null);

  constructor(private tokenService: TokenService) {
    if (tokenService.possuiToken()) {
      this.decodificarJWT();
    }
   }

  decodificarJWT() {
    const token = this.tokenService.retornarToken();
    const payload = jwtDecode<any>(token);

    const user: Cliente = {
      username: payload.sub,
      role: payload.role,
      id: '',
      password: '',
      email: '',
      email_validated: true,
      last_email_sent: payload.last_email_sent
    }

    this.userSubject.next(user);
  }

  retornarUser() {
    return this.userSubject.asObservable();
  }

  salvarToken(token: string) {
    this.tokenService.salvarToken(token);
    this.decodificarJWT();
  }

  logout() {
    this.tokenService.excluirToken();
    this.userSubject.next(null);
    
  }

  estaLogado() {
    return this.tokenService.possuiToken();
  }
}
