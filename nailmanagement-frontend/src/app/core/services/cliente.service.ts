import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { Cliente } from '../types/type';

@Injectable({
  providedIn: 'root',
})
export class ClienteService {
  private apiUrl: string = environment.apiUrl;
  constructor(private http: HttpClient) {}

  register(cliente: any): Observable<Cliente> {
    return this.http.post<Cliente>(`${this.apiUrl}/client`, cliente);
  }

  registerNoAuth(cliente: any): Observable<Cliente> {
    return this.http.post<Cliente>(`${this.apiUrl}/client/register`, cliente);
  }

  get(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${this.apiUrl}/client`);
  }

  getById(id: string) {
    return this.http.get<Cliente>(`${this.apiUrl}/client?id=${id}`);
  }

  patch(id: string, cliente: any): Observable<Cliente> {
    return this.http.patch<Cliente>(`${this.apiUrl}/client?id=${id}`, cliente);
  }

  delete(id: string) {
    return this.http.delete<Cliente>(`${this.apiUrl}/client?id=${id}`);
  }

  enviarCodigoConfirmacaoEmail(body: any) {
    return this.http.post<Cliente>(
      `${this.apiUrl}/client/email/confirmation/send`,
      body,
    );
  }

  receberCodigoConfirmacaoEmail(numero: string, body: any) {
    return this.http.post<Cliente>(
      `${this.apiUrl}/client/email/confirmation/validate`,
      body,
    );
  }
}
