import { Component, OnInit, inject } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { MatList, MatListItem } from '@angular/material/list';
import { ClienteService } from '../../core/services/cliente.service';
import { Cliente } from '../../core/types/type';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import {
  AbstractControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
  ɵInternalFormsSharedModule,
} from '@angular/forms';

@Component({
  selector: 'app-cliente',
  standalone: true,
  imports: [
    MatList,
    MatListItem,
    MatIcon,
    MatSelectModule,
    MatFormFieldModule,
    ɵInternalFormsSharedModule,
    ReactiveFormsModule,
  ],
  templateUrl: './cliente.component.html',
  styleUrls: ['./cliente.component.css'],
})
export class ClienteComponent implements OnInit {
  private fb = inject(NonNullableFormBuilder);

  // Criando o formulário
  form = this.fb.group({
    username: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    role: ['USER'],
    numeroConfirmacaoEmail: ['', [Validators.required]],
    // Grupo de senhas com validador customizado
    security: this.fb.group(
      {
        novaSenha: [''],
        confirmarSenha: [''],
      },
      { validators: this.compararSenhas },
    ),
  });

  compararSenhas(control: AbstractControl): ValidationErrors | null {
    const nova = control.get('novaSenha')?.value;
    const confirma = control.get('confirmarSenha')?.value;
    return nova === confirma ? null : { senhasDiferentes: true };
  }

  private clienteService = inject(ClienteService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  ultimoEmailEnviado!: string;

  abaAtual: string = 'lista';
  clientes: Cliente[] = [];
  clienteSelecionado: any = null;
  clienteParaExcluir: Cliente | null = null;
  clienteParaConfirmarEmail: Cliente | null = null;

  ngOnInit(): void {
    // Carrega a lista primeiro
    this.clienteService.get().subscribe({
      next: (data) => {
        this.clientes = data;
        // Após carregar os dados, checa se tem ID na URL para preencher a edição
        this.verificarParametros();
      },
      error: (err) => console.log(err),
    });
    // Ouve mudanças na URL caso o usuário navegue enquanto a página já está aberta
    this.route.queryParamMap.subscribe(() => this.verificarParametros());

    this.form.get('role')?.valueChanges.subscribe((role) => {
      const securityGroup = this.form.get('security');
      if (role === 'ADMIN') {
        securityGroup
          ?.get('novaSenha')
          ?.setValidators([Validators.minLength(6)]);
        securityGroup?.get('confirmarSenha')?.setValidators([]);
      } else {
        securityGroup?.get('novaSenha')?.clearValidators();
        securityGroup?.get('confirmarSenha')?.clearValidators();
      }
      securityGroup?.get('novaSenha')?.updateValueAndValidity();
      securityGroup?.get('confirmarSenha')?.updateValueAndValidity();
    });
  }

  private verificarParametros() {
    const id = this.route.snapshot.queryParamMap.get('id');
    const aba = this.route.snapshot.queryParamMap.get('aba');

    if (id && this.clientes.length > 0) {
      this.abaAtual = 'novo';
      this.clienteSelecionado =
        this.clientes.find((c) => String(c.id) === String(id)) || null;
      if (this.clienteSelecionado.last_email_sent) {
        this.ultimoEmailEnviado =
          this.clienteSelecionado.last_email_sent.substring(0, 10);
      } else {
        this.ultimoEmailEnviado = 'dd/mm/aaaa';
      }

      this.form.patchValue({
        username: this.clienteSelecionado.username,
        email: this.clienteSelecionado.email,
        role: this.clienteSelecionado.role || 'USER',
      });
    } else {
      this.abaAtual = aba || 'lista';
      if (!id) {
        this.clienteSelecionado = null;
        this.ultimoEmailEnviado = 'dd/mm/aaaa';
        this.form.setValue({
          username: '',
          email: '',
          role: 'USER',
          numeroConfirmacaoEmail: '',
          security: { confirmarSenha: '', novaSenha: '' },
        });
      }
    }
  }

  navegarPara(aba: string, id?: string) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { aba: aba, id: id || null },
      queryParamsHandling: 'merge',
    });
  }

  salvarCliente() {
    // Se true, é edição; Se false, é criação.
    if (this.clienteSelecionado) {
      const dados = this.form.getRawValue();
      let request = {
        username: this.form.value.username,
        role: this.form.value.role,
        email: this.form.value.email,
        password: dados.security.novaSenha || null,
      };

      this.clienteService.patch(this.clienteSelecionado.id, request).subscribe({
        error: (err) => {
          console.log(err);
          alert('Erro ao atualizar cliente!');
        },
      });
    } else {
      let request = {
        username: this.form.value.username,
        role: this.form.value.role,
        email: this.form.value.email,
        password: this.form.value.security?.novaSenha || null,
      };
      console.log(request);
      this.clienteService.register(request).subscribe({
        error: (err) => {
          console.log(err);
          alert('Erro ao cadastrar cliente!');
        },
      });
    }
  }

  confirmarExclusao(cliente: Cliente) {
    this.clienteParaExcluir = cliente;
  }

  deletarCliente() {
    this.clienteService.delete(this.clienteParaExcluir!.id).subscribe({
      next: () => {
        this.clienteParaExcluir = null;
        this.ngOnInit();
      },
      error: (err) => {
        console.log(err);
        alert('Erro ao deletar cliente!');
      },
    });
  }

  enviarCodigoConfirmacaoEmail() {
    const body = {
      username: this.clienteSelecionado.username,
      email: this.clienteSelecionado.email,
      givenCode: null,
    };

    this.clienteService.enviarCodigoConfirmacaoEmail(body).subscribe({
      error: (err) => {
        console.log(err);
        alert('Erro ao enviar código!');
      },
    });
  }

  confirmarEmail(cliente: Cliente) {
    this.clienteParaConfirmarEmail = cliente;
  }

  confirmacaoEmail(numero: string) {
    const body = {
      username: this.clienteParaConfirmarEmail?.username,
      email: this.clienteParaConfirmarEmail?.email,
      givenCode: this.form.value.numeroConfirmacaoEmail,
    };
    this.clienteService.receberCodigoConfirmacaoEmail(numero, body).subscribe({
      next: (data) => {
        if (data) {
          this.clienteParaConfirmarEmail = null;
          this.ngOnInit();
        } else {
          alert('Código de confirmação incorreto ou expirado!');
        }
      },
      error: (err) => {
        console.log(err);
        alert('Erro ao enviar código!');
      },
    });
  }
}
