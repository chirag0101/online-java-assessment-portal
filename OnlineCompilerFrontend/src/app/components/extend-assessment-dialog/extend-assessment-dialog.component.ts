import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field'; 
import { MatInputModule } from '@angular/material/input'; 
import { MatButtonModule } from '@angular/material/button'; 
import { min } from 'rxjs';

@Component({
  selector: 'app-extend-assessment-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './extend-assessment-dialog.component.html',
  styleUrls: ['./extend-assessment-dialog.component.css']
})
export class ExtendAssessmentDialogComponent {
  minutes: number | null = null;

  constructor(
    public dialogRef: MatDialogRef<ExtendAssessmentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { message: string }
  ) {}

  onConfirm(): void {
    if (this.minutes !== null && this.minutes > 0) {
      this.dialogRef.close(this.minutes); 
    }
  }

  onCancel(): void {
    this.dialogRef.close(null);
  }
}