import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatRadioModule } from '@angular/material/radio';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider'

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { UnidadeFederativa } from '../../core/types/type';
import { FormularioService } from '../../core/services/formulario.service';
import { FormValidations } from '../form-validations';
import { DropdownUfComponent } from '../dropdown-uf/dropdown-uf.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-base-form',
  imports: [CommonModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatAutocompleteModule, MatDatepickerModule, MatCheckboxModule, MatRadioModule, MatSlideToggleModule, MatButtonModule, MatIconModule, ReactiveFormsModule, MatCardModule, MatDividerModule,  DropdownUfComponent],
  templateUrl: './base-form.component.html',
  styleUrl: './base-form.component.css'
})
export class BaseFormComponent implements OnInit{
  cadastroForm!: FormGroup;
  estadoControl = new FormControl<UnidadeFederativa | null>(null, Validators.required);

  @Input() perfilComponent: boolean = false;
  @Input() titulo: string = 'Crie sua conta'; 
  @Input() textoBotao: string = 'CADASTRAR';
  @Output() acaoClique: EventEmitter<any> = new EventEmitter<any>()
  @Output() sair: EventEmitter<any> = new EventEmitter<any>()

  constructor(
    private formBuilder: FormBuilder,
    private formularioService: FormularioService
  ) { }

  ngOnInit() {
    this.cadastroForm = this.formBuilder.group({
      nome: [null, Validators.required],
      nascimento: [null, [Validators.required]],
      cpf: [null, [Validators.required]],
      cidade: [null, Validators.required],
      email: [null, [Validators.required, Validators.email]],
      senha: [null, [Validators.required, Validators.minLength(3)]],
      genero: ['outro'],
      telefone: [null, Validators.required],
      estado: this.estadoControl,
      confirmarEmail: [null, [Validators.required, Validators.email, FormValidations.equalTo('email')]],
      confirmarSenha: [null, [Validators.required, Validators.minLength(3), FormValidations.equalTo('senha')]],
      aceitarTermos: [false, [Validators.requiredTrue]]
    });

    if(this.perfilComponent){
      this.cadastroForm.get('aceitarTermos')?.setValidators(null)
    } else {
      this.cadastroForm.get('aceitarTermos')?.setValidators([Validators.requiredTrue])
    }

    this.cadastroForm.get('aceitarTermos')?.updateValueAndValidity();

    this.formularioService.setCadastro(this.cadastroForm)
  }

  executarAcao() {
    this.acaoClique.emit();
  }

  deslogar() {
    this.sair.emit();
  }
}
