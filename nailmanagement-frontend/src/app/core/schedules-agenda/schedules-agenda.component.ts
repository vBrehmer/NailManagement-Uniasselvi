import { Component } from '@angular/core';
import { CalendarComponent } from '../calendar/calendar.component';
import { MatListModule } from '@angular/material/list';
import { MatIcon } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { CadastroAgendaService } from '../services/cadastro-agenda.service';
import { AgendaResponse } from '../types/type';
import { SchedulesInDayComponent } from '../schedules-in-day/schedules-in-day.component';

@Component({
  selector: 'app-schedules-agenda',
  imports: [CalendarComponent, MatListModule, MatIcon],
  templateUrl: './schedules-agenda.component.html',
  styleUrl: './schedules-agenda.component.css',
})
export class SchedulesAgendaComponent {
  agendaParaExcluir: AgendaResponse | null = null;
  diaAgendamento: Date = new Date();
  hoje = new Date();

  constructor(
    private dialog: MatDialog,
    private agendaService: CadastroAgendaService,
  ) {}

  ngOnInit(): void {
    this.carregarDados();
    this.agendaService.agendaAtualizada$.subscribe(() => {
      this.carregarDados();
    });
  }

  agenda: AgendaResponse[] = [];

  abrirModal(evento?: any) {
    if (evento) {
      const dialogRef = this.dialog.open(SchedulesInDayComponent, {
        data: {
          id: evento.id,
          data: new Date(evento.schedule),
          cliente: evento.client_name,
          servico: evento.procedure_name,
          agente: evento.agent_name,
          evento: evento.id,
          hora: evento.schedule.substring(11, 18),
        },
      });
    } else {
      const dialogRef = this.dialog.open(SchedulesInDayComponent, {
        data: { id: null },
      });
      dialogRef.afterClosed().subscribe((result) => {
        if (result) {
          setTimeout(() => {
            this.carregarDados();
          }, 500);
        }
      });
    }
  }

  carregarDados() {
    this.diaAgendamento = this.hoje;
    this.hoje.setHours(0, 0, 0, 0);
    this.agendaService.getByScheduleBetween(this.hoje.toISOString()).subscribe({
      next: (data) => {
        this.agenda = data;
      },
      error: (err) => console.log(err),
    });
  }

  buscarData(i: any) {
    this.diaAgendamento.setHours(0, 0, 0, 0);
    const novaData = new Date(this.diaAgendamento);
    novaData.setDate(this.diaAgendamento.getDate() + i);
    this.diaAgendamento = novaData;
    this.agendaService
      .getByScheduleBetween(this.diaAgendamento.toISOString())
      .subscribe({
        next: (data) => {
          this.agenda = data;
        },
        error: (err) => console.log(err),
      });
  }

  confirmarExclusao(agenda: AgendaResponse) {
    this.agendaParaExcluir = agenda;
  }

  deletarAgenda() {
    this.agendaService.delete(this.agendaParaExcluir!.id).subscribe({
      next: () => {
        this.agendaParaExcluir = null;
        this.ngOnInit();
      },
      error: (err) => {
        console.log(err);
        alert('Erro ao deletar agenda!');
      },
    });
  }

  today() {
    this.diaAgendamento = this.hoje;
    this.carregarDados();
  }
}
