import { Component, inject, OnInit } from '@angular/core';
import { Agente } from '../../core/types/type';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { AgenteService } from '../../core/services/agente.service';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-agente',
  standalone: true,
  imports: [CommonModule, MatListModule, MatIconModule, ReactiveFormsModule],
  templateUrl: './agente.component.html',
  styleUrl: './agente.component.css'
})
export class AgenteComponent implements OnInit {
  private fb = inject(NonNullableFormBuilder);
  private agenteService = inject(AgenteService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  agentes: Agente[] = [];
  agenteSelecionado: Agente | null = null;
  agenteParaExcluir: Agente | null = null;
  abaAtual: string = 'lista';

  form = this.fb.group({
    name: ['', Validators.required],
    area: ['Manicure & Pedicure', Validators.required]
  });

  ngOnInit(): void {
        this.agenteService.get().subscribe({
      next: (data) => {
        this.agentes = data;
        this.verificarParametros(); // Checa se deve abrir edição após carregar lista
      },
      error: (err) => console.log(err)
    });
    // Ouve mudanças na URL para navegação entre Abas e Edição
    this.route.queryParamMap.subscribe(() => this.verificarParametros());
  }

  private verificarParametros() {
    const id = this.route.snapshot.queryParamMap.get('id');
    const aba = this.route.snapshot.queryParamMap.get('aba');

    if (id && this.agentes.length > 0) {
      this.abaAtual = 'novo';
      this.agenteSelecionado = this.agentes.find(a => String(a.id) === String(id)) || null;
      
      if (this.agenteSelecionado) {
        this.form.patchValue({
          name: this.agenteSelecionado.name,
          area: this.agenteSelecionado.area
        });
      }
    } else {
      this.abaAtual = aba || 'lista';
      if (!id) {
        this.agenteSelecionado = null;
        this.form.reset({ area: 'Manicure & Pedicure' });
      }
    }
  }

  navegarPara(aba: string, id?: string) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { aba: aba, id: id || null },
      queryParamsHandling: 'merge'
    });
  }

  salvarProfissional() {
    const dados = this.form.getRawValue();
    
    if (this.agenteSelecionado) {
      // Lógica de Edição (Patch)
      this.agenteService.patch(this.agenteSelecionado.id, dados).subscribe({
        next: () => {
          this.navegarPara('lista');
          this.ngOnInit();
        },
        error: (err) => alert('Erro ao atualizar')
      });
    } else {
      // Lógica de Cadastro
      this.agenteService.register(dados).subscribe({
        next: () => {
          this.navegarPara('lista');
          this.ngOnInit();
        },
        error: (err) => alert('Erro ao cadastrar')
      });
    }
  }

  confirmarExclusao(agente: Agente) {
    this.agenteParaExcluir = agente;
  }

  deletarAgente() {
    if (this.agenteParaExcluir) {
      this.agenteService.delete(this.agenteParaExcluir.id).subscribe({
        next: () => {
          this.agenteParaExcluir = null;
          this.ngOnInit();
        },
        error: () => alert('Erro ao excluir')
      });
    }
  }
}