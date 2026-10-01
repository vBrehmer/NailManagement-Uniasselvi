import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Servico } from '../types/type';

@Injectable({
  providedIn: 'root'
})
export class ServicoService {
  private apiUrl: string = environment.apiUrl;
  constructor(private http: HttpClient) { }

  register(servico: any): Observable<Servico> {
    return this.http.post<Servico>(`${this.apiUrl}/procedure`, servico);
  }

  get(): Observable<Servico[]> {
    return this.http.get<Servico[]>(`${this.apiUrl}/procedure`);
  }

  getById(id: string) {
    return this.http.get<Servico>(`${this.apiUrl}/procedure?id=${id}`);
  }

  patch(id: string, service: any): Observable<Servico> {
    return this.http.patch<Servico>(`${this.apiUrl}/procedure?id=${id}`, service);
  }

  delete(id: string) {
    return this.http.delete<Servico>(`${this.apiUrl}/procedure?id=${id}`);
  }
}
