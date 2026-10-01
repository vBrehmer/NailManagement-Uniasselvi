import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import {
  MatAutocompleteModule,
  MatAutocompleteSelectedEvent,
} from '@angular/material/autocomplete';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatRadioModule } from '@angular/material/radio';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';

import { AsyncPipe, CommonModule } from '@angular/common';
import {
  Component,
  Inject,
  Input,
  OnInit,
  ViewEncapsulation,
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { FormularioService } from '../services/formulario.service';
import { CadastroAgendaService } from '../services/cadastro-agenda.service';
import { Cliente, Agente, Servico, Agenda } from '../types/type';
import { AgenteService } from '../services/agente.service';
import { ClienteService } from '../services/cliente.service';
import { ServicoService } from '../services/servico.service';

@Component({
  selector: 'app-schedules-in-day',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDatepickerModule,
    MatCheckboxModule,
    MatRadioModule,
    MatSlideToggleModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatDividerModule,
    AsyncPipe,
  ],
  templateUrl: './schedules-in-day.component.html',
  styleUrl: './schedules-in-day.component.css',
  encapsulation: ViewEncapsulation.None,
})
export class SchedulesInDayComponent implements OnInit {
  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: {
      id: string;
      data: Date;
      cliente: string;
      servico: string;
      agente: string;
      evento: any;
      hora: string;
    },
    private formBuilder: FormBuilder,
    private formularioService: FormularioService,
    private cadastroAgendaService: CadastroAgendaService,
    private agenteService: AgenteService,
    private clienteService: ClienteService,
    private servicoService: ServicoService,
    private dialogRef: MatDialogRef<SchedulesInDayComponent>,
  ) {
    // Impede sair do dialog clicando fora ou ESC caso tenha algo preenchido
    dialogRef.backdropClick().subscribe(() => {
      if (this.cadastroForm.dirty) {
        return;
      }
      this.dialogRef.close();
    });
  }

  hoje = new Date();

  ngOnInit() {
    //Configuração pra precisar de campos preenchidos
    this.cadastroForm = this.formBuilder.group({
      cliente: [null, [Validators.required]],
      hora: [null, [Validators.required]],
      data: [null, [Validators.required]],
      servico: [null, [Validators.required]],
      agente: [null, [Validators.required]],
    });

    this.formularioService.setCadastro(this.cadastroForm);

    if (this.data.data && this.data.hora) {
      this.cadastroForm.patchValue({
        data: this.data.data,
        hora: this.data.hora,
      });
    } else if (this.data.data) {
      this.cadastroForm.patchValue({
        data: this.data.data,
      });
    }

    this.clienteService.get().subscribe({
      next: (data) => {
        this.clientes = data;

        if (this.data.id) {
          const clienteEncontrado = this.clientes.find(
            (a) => a.username.toLowerCase() === this.data.cliente.toLowerCase(),
          );
          this.clienteCtrl.setValue(clienteEncontrado!);
          this.clienteCtrl.updateValueAndValidity();
        }

        // Configuração da filtragem do cliente quando algo é digitado
        this.clientesFiltrados$ = this.clienteCtrl.valueChanges.pipe(
          startWith(this.clienteCtrl.value || ''),
          map((value) => {
            const nome =
              typeof value === 'string' ? value : (value?.username ?? '');
            return this.filtrarClientes(nome);
          }),
        );
      },
      error: (err) => console.log(err),
    });

    this.servicoService.get().subscribe({
      next: (data) => {
        this.servicos = data;

        if (this.data.id) {
          const servicoEncontrado = this.servicos.find(
            (a) => a.name.toLowerCase() === this.data.servico.toLowerCase(),
          );
          this.servicoCtrl.setValue(servicoEncontrado!);
          this.servicoCtrl.updateValueAndValidity();
        }

        // Configuração da filtragem do serviço quando algo é digitado
        this.servicoFiltrados$ = this.servicoCtrl.valueChanges.pipe(
          startWith(this.servicoCtrl.value || ''),
          map((value) => {
            const nome =
              typeof value === 'string' ? value : (value?.name ?? '');
            return this.filtrarServicos(nome);
          }),
        );
      },
      error: (err) => console.log(err),
    });

    this.agenteService.get().subscribe({
      next: (data) => {
        this.agentes = data;

        if (this.data.id) {
          const agenteEncontrado = this.agentes.find(
            (a) => a.name.toLowerCase() === this.data.agente.toLowerCase(),
          );
          this.agenteCtrl.setValue(agenteEncontrado!);
          this.agenteCtrl.updateValueAndValidity();
        }
        // Configuração da filtragem do agente quando algo é digitado
        this.agentesFiltrados$ = this.agenteCtrl.valueChanges.pipe(
          startWith(this.agenteCtrl.value || ''),
          map((value) => {
            const nome =
              typeof value === 'string' ? value : (value?.name ?? '');
            return this.filtrarAgentes(nome);
          }),
        );
      },
      error: (err) => console.log(err),
    });
  }

  compararObjetos(o1: any, o2: any): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  cadastroForm!: FormGroup;
  // Configuração do input do cliente e filtragem
  clienteCtrl = new FormControl<Cliente | null>(null, Validators.required);
  clientes: Cliente[] = [];

  clientesFiltrados$!: Observable<Cliente[]>;

  filtrarClientes(valor: string): Cliente[] {
    const filter = valor.toLowerCase().trim();
    return this.clientes.filter((c) =>
      c.username.toLowerCase().includes(filter),
    );
  }

  displayCliente(client: Cliente): string {
    return client && client.username ? client.username : '';
  }

  clienteSelecionado(event: MatAutocompleteSelectedEvent) {
    this.cadastroForm.markAsDirty();
    this.clienteCtrl.setValue(event.option.value);
  }
  // Configuração do input do serviço e filtragem
  servicoCtrl = new FormControl<Servico | null>(null, Validators.required);
  servicos: Servico[] = [];

  servicoFiltrados$!: Observable<Servico[]>;

  filtrarServicos(valor: string): Servico[] {
    const filter = valor.toLowerCase();
    return this.servicos.filter((s) => s.name.toLowerCase().includes(filter));
  }

  displayServico(servico: Servico): string {
    return servico ? servico.name : '';
  }

  servicoSelecionado(event: MatAutocompleteSelectedEvent) {
    this.cadastroForm.markAsDirty();
    this.servicoCtrl.setValue(event.option.value);
  }

  // Configuração do input do agente e filtragem
  agenteCtrl = new FormControl<Agente | null>(null, Validators.required);
  agentes: Agente[] = [];

  agentesFiltrados$!: Observable<Agente[]>;

  filtrarAgentes(valor: string): Agente[] {
    const filter = valor.toLowerCase();
    return this.agentes.filter((a) => a.name.toLowerCase().includes(filter));
  }

  displayAgentes(agente: Agente): string {
    return agente ? agente.name : '';
  }

  agenteSelecionado(event: MatAutocompleteSelectedEvent) {
    this.cadastroForm.markAsDirty();
    this.agenteCtrl.setValue(event.option.value);
  }

  closeModal() {
    document
      .querySelectorAll('.fc-daygrid-day.selected')
      .forEach((el) => el.classList.remove('selected'));
    this.dialogRef.close(true);
  }

  @Input() event: any;

  registerSchedule() {
    if (!this.data.id) {
      const agenda: Agenda = {
        client_id: this.clienteCtrl.value!.id,
        procedure_id: this.servicoCtrl.value!.id,
        agent_id: this.agenteCtrl.value!.id,
        schedule: `${this.cadastroForm.value.data.toISOString().substring(0, 10)}T${this.cadastroForm.value.hora}:00-03:00`,
      };

      this.cadastroAgendaService.register(agenda).subscribe({
        next: () => {
          this.cadastroAgendaService.notificarMudanca();
          this.closeModal();
        },
        error: (err) => {
          console.error(err);
          alert(`Erro ao cadastrar agenda! ${err.error.message}`);
        },
      });
    } else {
      const agenda = {
        id: this.data.id,
        client_id: this.clienteCtrl.value!.id,
        procedure_id: this.servicoCtrl.value!.id,
        agent_id: this.agenteCtrl.value!.id,
        schedule: `${this.cadastroForm.value.data.toISOString().substring(0, 10)}T${this.cadastroForm.value.hora}:00-03:00`,
      };

      this.cadastroAgendaService.patch(agenda.id, agenda).subscribe({
        next: () => {
          this.cadastroAgendaService.notificarMudanca();
          this.closeModal();
        },
        error: (err) => {
          console.error(err);
          alert(`Erro ao cadastrar agenda! ${err.error.message}`);
        },
      });
    }
  }
}
