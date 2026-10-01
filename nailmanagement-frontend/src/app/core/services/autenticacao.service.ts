import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { UserService } from './user.service';

interface AuthResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AutenticacaoService {

  private apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient,
    private userService: UserService
  ) {}

  autenticar(login:string, password: string): Observable<HttpResponse<AuthResponse>> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, 
      {login, password}, 
      {observe: 'response'}).pipe(
      tap((response) => {
        const authToken = response.body?.token || '';
        this.userService.salvarToken(authToken);
      })
    )
  }
}
