import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

export interface StatusDialogData {
  status: 'loading' | 'success' | 'error';
  message?: string;
}

@Component({
  selector: 'app-status-dialog',
  imports: [CommonModule, MatDialogModule, MatIconModule, MatProgressSpinnerModule],
  templateUrl: './status-dialog.component.html',
  styleUrl: './status-dialog.component.css'
})
export class StatusDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: StatusDialogData) {}
}
