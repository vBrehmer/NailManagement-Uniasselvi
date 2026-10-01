import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { Servico } from '../../core/types/type';
import { ServicoService } from '../../core/services/servico.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-servico',
  standalone: true,
  imports: [CommonModule, MatListModule, MatIconModule, ReactiveFormsModule],
  templateUrl: './servico.component.html',
  styleUrl: './servico.component.css'
})
export class ServicoComponent implements OnInit {
  private fb = inject(NonNullableFormBuilder);
  private servicoService = inject(ServicoService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  abaAtual: string = 'lista';
  servicos: Servico[] = [];
  servicoSelecionado: Servico | null = null;
  servicoParaExcluir: Servico | null = null;

  form = this.fb.group({
    name: ['', Validators.required],
    value: [0, [Validators.required, Validators.min(1)]]
  });

  ngOnInit(): void {
    this.carregarServicos();
    this.route.queryParamMap.subscribe(() => this.verificarParametros());
  }

  carregarServicos() {
    this.servicoService.get().subscribe({
      next: (data) => {
        this.servicos = data;
        this.verificarParametros();
      },
      error: (err) => console.log(err)
    });
  }

  private verificarParametros() {
    const id = this.route.snapshot.queryParamMap.get('id');
    const aba = this.route.snapshot.queryParamMap.get('aba');

    if (id && this.servicos.length > 0) {
      this.abaAtual = 'novo';
      this.servicoSelecionado = this.servicos.find(s => String(s.id) === String(id)) || null;

      if (this.servicoSelecionado) {
        this.form.patchValue({
          name: this.servicoSelecionado.name,
          value: this.servicoSelecionado.value
        });
      }
    } else {
      this.abaAtual = aba || 'lista';
      if (!id) {
        this.servicoSelecionado = null;
        this.form.reset({ value: 0 });
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

  salvarServico() {
    const dados = this.form.getRawValue();

    if (this.servicoSelecionado) {
      this.servicoService.patch(this.servicoSelecionado.id, dados).subscribe({
        next: () => {
          this.carregarServicos();
          this.navegarPara('lista');
        },
        error: (err) => alert('Erro ao atualizar serviço')
      });
    } else {
      this.servicoService.register(dados).subscribe({
        next: () => {
          this.carregarServicos();
          this.navegarPara('lista');
        },
        error: (err) => alert('Erro ao cadastrar serviço')
      });
    }
  }

  confirmarExclusao(servico: Servico) {
    this.servicoParaExcluir = servico;
  }

  deletarServico() {
    if (this.servicoParaExcluir) {
      this.servicoService.delete(this.servicoParaExcluir.id).subscribe({
        next: () => {
          this.servicoParaExcluir = null;
          this.carregarServicos();
        },
        error: () => alert('Erro ao excluir serviço')
      });
    }
  }
}