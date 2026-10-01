import { Component, inject } from '@angular/core';
import {
  AbstractControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { ClienteService } from '../../core/services/cliente.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  private fb = inject(NonNullableFormBuilder);
  private router = inject(Router);

  constructor(private clienteService: ClienteService) {}

  registrarForm = this.fb.group({
    username: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    security: this.fb.group(
      {
        senha: ['', [Validators.required]],
        confirmarSenha: [''],
      },
      { validators: this.compararSenhas },
    ),
  });

  compararSenhas(control: AbstractControl): ValidationErrors | null {
    const nova = control.get('senha')?.value;
    const confirma = control.get('confirmarSenha')?.value;
    return nova === confirma ? null : { senhasDiferentes: true };
  }

  registrar() {
    let dados = this.registrarForm.getRawValue();
    let request = {
      username: dados.username,
      email: dados.email.trim(),
      password: dados.security.senha,
      role: 'USER',
    };
    this.clienteService.registerNoAuth(request).subscribe({
      next: () => this.router.navigate(['/login']),
      error: (err) => {
        if (
          err.status === 500 &&
          err.error.message.includes(`Key (username)=(${request.username})`)
        ) {
          console.log(err);
          alert(`Já existe um usuário com o nome "${request.username}"!`);
        } else {
          console.log(err);
          alert('Erro ao realizar registro!');
        }
      },
    });
  }
}
