import { Component, inject } from '@angular/core';
import { UserService } from '../../core/services/user.service';
import { AutenticacaoService } from '../../core/services/autenticacao.service';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  private fb = inject(NonNullableFormBuilder);
  private router = inject(Router);

  constructor(private authService: AutenticacaoService) {}

  loginForm = this.fb.group({
    usuario: ['', [Validators.required]],
    senha: ['', [Validators.required]],
  });

  login() {
    if (this.loginForm.valid) {
      const dados = this.loginForm.getRawValue();
      this.authService.autenticar(dados.usuario, dados.senha).subscribe({
        next: () => {
          console.log('Login realizado com sucesso!');
          this.router.navigate(['/agenda']);
        },
        error: (err) => {
          if (err.status === 403 || err.status === 500) {
            alert('Usuário ou senha inválidos!');
          } else if (err.status === 400) {
            alert('Permissão insuficiente! Necessário permissão "ADMIN"');
          } else if (err.status === 401) {
            alert('Bearer token inválido. Entre em contato conosco!');
          } else {
            alert('Erro no servidor. Tente novamente mais tarde.');
          }
        },
      });
    }
  }
}
