import { Component, OnInit, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatRadioModule } from '@angular/material/radio';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { AssessmentService } from '../../services/assessment.service';
import { CandidateDetails } from '../../models/candidate-details.model';
import { Router } from '@angular/router';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-candidate-search-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    MatRadioModule,
    MatIconModule
  ],
  templateUrl: './candidate-search-dialog.component.html',
  styleUrls: ['./candidate-search-dialog.component.css']
})
export class CandidateSearchDialogComponent implements OnInit {
  searchTerm: string = '';
  filteredCandidates: CandidateDetails[] = [];
  displayedColumns: string[] = ['select', 'candidateId', 'candidateFullName', 'candidateTechnology', 'candidateYearsOfExpInMonths','candidateEmailId'];
  selectedCandidate: CandidateDetails | null = null;
  selectedCandidateId: string | null = null;

  private allCandidates: CandidateDetails[] = [];

  constructor(
    public dialogRef: MatDialogRef<CandidateSearchDialogComponent>,
    private adminService:AdminService,
    private router:Router,
    private assessmentService: AssessmentService,
    @Inject(MAT_DIALOG_DATA) public data: { message: string }
  ) {}

  ngOnInit(): void {
    this.assessmentService.getAllCandidates().subscribe({
      next: (response) => {
        this.allCandidates = response.response;
        this.filteredCandidates = [...this.allCandidates];
      },
      error: (error) => {
        this.filteredCandidates = [];
        this.adminService.onSessionTimeout();
      }
    });
  }

  performSearch(): void {
    const lowerCaseSearchTerm = this.searchTerm.toLowerCase();
    this.filteredCandidates = this.allCandidates.filter(candidate =>
      candidate.candidateFullName?.toLowerCase().includes(lowerCaseSearchTerm) ||
      candidate.candidateId?.toLowerCase().includes(lowerCaseSearchTerm) ||
      candidate.candidateTechnology?.toLowerCase().includes(lowerCaseSearchTerm) ||
      candidate.candidateEmailId?.toLowerCase().includes(lowerCaseSearchTerm)
    );

    if (this.selectedCandidateId) {
      const currentSelectionInFiltered = this.filteredCandidates.find(c => c.candidateId === this.selectedCandidateId);
      if (!currentSelectionInFiltered) {
        this.selectedCandidate = null;
        this.selectedCandidateId = null;
      } else {
        this.selectedCandidate = currentSelectionInFiltered;
      }
    }
  }

  onSelectionChange(candidateId: string): void {
    const selected = this.filteredCandidates.find(c => c.candidateId === candidateId);
    this.selectedCandidate = selected || null;
  }

  onConfirm(): void {
    this.dialogRef.close(this.selectedCandidate);
  }

  onCancel(): void {
    this.dialogRef.close(null);
  }
}
