import { Component } from '@angular/core';
import { ProfessionalsAgendaComponent } from '../../core/professionals-agenda/professionals-agenda.component';
import { MatListModule } from '@angular/material/list';
import { SchedulesAgendaComponent } from '../../core/schedules-agenda/schedules-agenda.component';

@Component({
  selector: 'app-agenda',
  imports: [ProfessionalsAgendaComponent, MatListModule, SchedulesAgendaComponent],
  templateUrl: './agenda.component.html',
  styleUrl: './agenda.component.css'
})
export class AgendaComponent {

}
