import { Injectable } from '@angular/core';
import { Agenda, AgendaResponse } from '../types/type';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CadastroAgendaService {
  private apiUrl: string = environment.apiUrl;
  private agendaAtualizada = new Subject<void>();
  agendaAtualizada$ = this.agendaAtualizada.asObservable();
  constructor(private http: HttpClient) {}

  register(agenda: Agenda): Observable<Agenda> {
    return this.http.post<Agenda>(`${this.apiUrl}/agenda`, agenda);
  }

  delete(id: string) {
    return this.http.delete<Agenda>(`${this.apiUrl}/agenda?id=${id}`);
  }

  get(): Observable<Agenda[]> {
    return this.http.get<Agenda[]>(`${this.apiUrl}/agenda`);
  }

  getBySchedule(schedule: string): Observable<Agenda[]> {
    return this.http.get<Agenda[]>(
      `${this.apiUrl}/agenda?schedule=${schedule}`,
    );
  }

  getByScheduleBetween(schedule: string): Observable<AgendaResponse[]> {
    return this.http.get<AgendaResponse[]>(
      `${this.apiUrl}/agenda?schedule=${schedule}&between=true`,
    );
  }

  patch(id: string, schedule: any): Observable<Agenda> {
    return this.http.patch<Agenda>(`${this.apiUrl}/agenda?id=${id}`, schedule);
  }

  notificarMudanca() {
    this.agendaAtualizada.next();
  }
}
