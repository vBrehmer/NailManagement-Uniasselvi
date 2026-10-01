import { Component, Input, ViewChild } from '@angular/core';
import {
  FullCalendarComponent,
  FullCalendarModule,
} from '@fullcalendar/angular';
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { SchedulesInDayComponent } from '../schedules-in-day/schedules-in-day.component';
import { MatDialog } from '@angular/material/dialog';
import { AgendaResponse } from '../types/type';

@Component({
  selector: 'app-calendar',
  imports: [FullCalendarModule],
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css',
})
export class CalendarComponent {
  @Input() agenda: AgendaResponse[] = [];

  constructor(private dialog: MatDialog) {}

  dataSelecionada!: Date;

  @ViewChild(FullCalendarComponent)
  calendar!: FullCalendarComponent;

  abrirModal() {
    const dialogRef = this.dialog.open(SchedulesInDayComponent, {
      data: {
        data: this.dataSelecionada,
      },
      disableClose: true,
    });
  }

  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin, interactionPlugin],
    initialView: 'dayGridMonth',
    locale: 'pt-br',
    dayMaxEvents: false,
    selectable: false,
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: '',
    },
    titleFormat: {
      year: 'numeric',
      month: 'long',
    },
    buttonIcons: {
      prev: 'chevron-left',
      next: 'chevron-right',
    },
    dateClick: (info: any) => {
      const day = info.date.getDay();
      const today = new Date().setHours(0, 0, 0, 0);

      // Se for domingo, não é possível adicionar e/ou alterar eventos
      if ([0].includes(day) || info.date < today) {
        return;
      }
      this.dataSelecionada = info.date;
      this.abrirModal();
      document
        .querySelectorAll('.fc-daygrid-day.select')
        .forEach((el) => el.classList.remove('selected'));

      info.dayEl.classList.add('selected');
    },
  };
}
