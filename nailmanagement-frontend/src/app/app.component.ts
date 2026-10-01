import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from './core/sidebar/sidebar.component';
import { Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [CommonModule, SidebarComponent, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'nail-management';

  private router = inject(Router);

  isLoginPage(): boolean {
    return this.router.url.includes('/login')
  }

  isRegisterPage(): boolean {
    return this.router.url.includes('/registrar')
  }
}
