import { Component, HostListener, inject, input, OnInit, signal } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list'
import { Router, RouterModule } from "@angular/router";
import { UserService } from '../services/user.service';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  imports: [MatIcon, MatListModule, RouterModule, AsyncPipe],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit{

  constructor(
      public userService: UserService
  ) {}

  private router = inject(Router);
  width = 150; // Largura inicial
  private isResizing = false;
  userName!: string;

  ngOnInit(): void {
    if (!this.router.url.includes('/login') || !this.router.url.includes('/registrar')) {
      this.carregarUserName();
    }
  }

  carregarUserName(): string {
    this.userService.retornarUser().subscribe({
      next: data => {
        this.userName = data?.username || '';
      },
      error: err => {
        console.log(err);
        this.userName = '';
      }
    })
    return '';
  }

  logout() {
    this.userService.logout();
    this.router.navigate(['/login']);
  }

  onMouseDown(event: MouseEvent) {
    this.isResizing = true;
    event.preventDefault();
  }

  @HostListener('window:mousemove', ['$event'])
  onMouseMove(event: MouseEvent) {
    if (!this.isResizing) return;

    // Atualiza a largura com base na posição X do mouse
    // Você pode subtrair o offset da sidebar se ela não estiver no pixel 0
    if (event.clientX >= 150 && event.clientX <= 500) {
      this.width = event.clientX;
    }
  }

  @HostListener('window:mouseup')
  onMouseUp() {
    this.isResizing = false;
  }

}
