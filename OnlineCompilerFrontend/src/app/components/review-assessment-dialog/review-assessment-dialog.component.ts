import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialog, // Added
} from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { ViewReport } from '../../models/response/view-report-res.model';
import { AssessmentService } from '../../services/assessment.service';
import { SubmitReportReq } from '../../models/request/submit-report-req.model';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component'; // Import your component

@Component({
  selector: 'app-review-assessment-dialog',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    MatDialogModule, 
    MatIconModule, 
    MatAutocompleteModule, 
    MatInputModule
  ],
  templateUrl: './review-assessment-dialog.component.html',
  styleUrl: './review-assessment-dialog.component.css',
})
export class ReviewAssessmentDialogComponent implements OnInit {
  isLoading = false;
  errorMessage: string = '';
  filteredTechs: string[][] = [];

  masterTechList: string[] = [
    "ANGULAR", "APPLICATION CONTAINERS", "COMMUNICATIONS", "FUNCTIONAL DOCUMENTATION",
    "HARDWARE", "JAVA", "JSON", "JSP", "REST API", "SECURITIES", "SPRING", "SQL",
    "STRUTS", "TECHNICAL DOCUMENTATION",
  ];

  constructor(
    public dialogRef: MatDialogRef<ReviewAssessmentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ViewReport,
    private assessmentService: AssessmentService,
    private router: Router,
    private dialog: MatDialog // Injected MatDialog
  ) {}

  ngOnInit() {
    if (!this.data.finalFeedback) this.data.finalFeedback = '';
    this.data.isReviewed = !!this.data.isReviewed;

    if (this.data.assessmentReports) {
      this.data.assessmentReports.forEach((r: any, index: number) => {
        r.isExisting = true;
        this.filteredTechs[index] = this.getAvailableTechs(index);
      });
    }
  }

  getAvailableTechs(currentIndex: number): string[] {
    const selectedInOtherRows = this.data.assessmentReports
      .filter((_, index) => index !== currentIndex)
      .map((r) => r.langType);

    return this.masterTechList.filter(
      (tech) => !selectedInOtherRows.includes(tech)
    );
  }

  filterTech(event: any, index: number): void {
    const query = event.target.value.toLowerCase();
    const available = this.getAvailableTechs(index);
    this.filteredTechs[index] = available.filter(tech => 
      tech.toLowerCase().includes(query)
    );
  }

  onTechSelected(event: any, index: number): void {
    this.data.assessmentReports[index].langType = event.option.value;
  }

  addNewRow(): void {
    if (this.data.assessmentReports.length < this.masterTechList.length) {
      const newRow = {
        langType: '',
        score: null as any,
        comments: '',
        isExisting: false,
      } as any;

      this.data.assessmentReports.push(newRow);
      const newIndex = this.data.assessmentReports.length - 1;
      this.filteredTechs[newIndex] = this.getAvailableTechs(newIndex);
    }
  }

  removeRow(index: number): void {
    this.data.assessmentReports.splice(index, 1);
    this.filteredTechs.splice(index, 1);
  }

  get currentAvgScore(): number {
    const reports = this.data.assessmentReports;
    const validScores = reports
      .map((r) => Number(r.score))
      .filter((s) => !isNaN(s) && s > 0);

    if (validScores.length === 0) return 0;
    const total = validScores.reduce((sum, s) => sum + s, 0);
    const avg = total / reports.length;
    return Math.round(avg * 10) / 10;
  }

  onAction(actionType: 'SELECT' | 'REJECT'): void {
    const hasEmptyTech = this.data.assessmentReports.some(
      (r) => !r.langType || r.score === null
    );

    if (hasEmptyTech) {
      this.errorMessage = 'Please ensure all technology rows have a selection and a score.';
      return;
    }

    // Open Confirmation Dialog
    const confirmRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '350px',
      data: { 
        message: `Are you sure you want to "${actionType.toUpperCase()}" this candidate?`,
        status:actionType
      }
    });

    confirmRef.afterClosed().subscribe((confirmed) => {
      if (confirmed) {
        this.submitReview(actionType);
      }
    });
  }

  private submitReview(actionType: 'SELECT' | 'REJECT'): void {
    this.isLoading = true;
    this.errorMessage = '';

    const request: SubmitReportReq = {
      candidateId: this.data.candidateId,
      isPassed: actionType === 'SELECT',
      reviews: this.data.assessmentReports.map((r) => ({
        langType: r.langType,
        score: r.score,
        feedback: r.comments,
      })),
      finalFeedback: this.data.finalFeedback,
      finalAvgScore: this.currentAvgScore,
      assessmentEndTime:this.data.assessmentEndTime,
      interviewerId:this.data.interviewerId,
      round:this.data.round
    };

    this.assessmentService.submitCandidateReview(request).subscribe({
      next: (res: any) => {
        this.isLoading = false;
        if (res?.status) this.dialogRef.close({ action: actionType });
        else this.errorMessage = res?.message || 'Submission failed.';
      },
      error: (err: any) => {
        this.isLoading = false;
        this.errorMessage = err?.error?.message || 'Server error occurred.';
      },
    });
  }

  fetchDetailedInfo(langType: string): void {
    this.dialogRef.close();
    this.router.navigate(['/viewSubmissions'], {
      state: { tech: langType, candidate: this.data.candidateId, round:this.data.round,interviewer: this.data.interviewerId,assessmentEndTime:this.data.assessmentEndTime},
    });
  }

  onClose() {
    this.dialogRef.close();
  }
}