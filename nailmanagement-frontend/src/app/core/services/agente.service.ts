import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Agente } from '../types/type';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AgenteService {

  private apiUrl: string = environment.apiUrl;
  constructor(private http: HttpClient) { }

  register(agente: any): Observable<Agente> {
    return this.http.post<Agente>(`${this.apiUrl}/agent`, agente);
  }

  get(): Observable<Agente[]> {
    return this.http.get<Agente[]>(`${this.apiUrl}/agent`);
  }

  getById(id: string) {
    return this.http.get<Agente>(`${this.apiUrl}/agent?id=${id}`);
  }

  patch(id: string, agente: any): Observable<Agente> {
    return this.http.patch<Agente>(`${this.apiUrl}/agent?id=${id}`, agente);
  }

  delete(id: string) {
    return this.http.delete<Agente>(`${this.apiUrl}/agent?id=${id}`)
  }
}
