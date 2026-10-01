import { Component, inject, OnInit } from '@angular/core';
import { Agente } from '../types/type';
import { AgenteService } from '../services/agente.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-professionals-agenda',
  imports: [],
  templateUrl: './professionals-agenda.component.html',
  styleUrl: './professionals-agenda.component.css'
})
export class ProfessionalsAgendaComponent implements OnInit{

  private router = inject(Router);

  constructor(
    private agenteService: AgenteService
  ) {}

  ngOnInit(): void {
    this.agenteService.get().subscribe({
      next: (data) => {
        this.profissionais = data;
      },
      error: (err) => {console.log('Erro! ', err)}
      
    })
  }
  profissionais: Agente[] = [];

  redirect(profissional: Agente) {
    this.router.navigate(['/profissional'], {
      queryParams: { aba: 'novo', id: profissional.id },
      queryParamsHandling: 'merge'
    });
  }

}
