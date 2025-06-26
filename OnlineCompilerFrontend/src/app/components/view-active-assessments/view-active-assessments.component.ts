import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import {
  AssessmentService,
  ActiveAssessment,
  ActiveAssessmentsResponse,
  ExtendAssessment,
  UrlRes
} from '../../services/assessment.service';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { ExtendAssessmentDialogComponent } from '../extend-assessment-dialog/extend-assessment-dialog.component';
import { MatIconModule } from '@angular/material/icon';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-view-active-assessments',
  standalone: true,
  imports: [CommonModule, HttpClientModule, MatIconModule],
  templateUrl: './view-active-assessments.component.html',
  styleUrl: './view-active-assessments.component.css',
})
export class ViewActiveAssessmentsComponent implements OnInit {
  activeAssessments: ActiveAssessment[] = [];
  errorMessage: string = '';
  isLoading: boolean = false;

  assessmentUrl: string = '';
  copySuccess: boolean = false;

  constructor(
    private assessmentService: AssessmentService,
    private router: Router,
    private dialog: MatDialog,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    this.fetchActiveAssessments();
  }

  fetchActiveAssessments(): void {
    this.isLoading = true;
    this.errorMessage = '';

    if (sessionStorage.getItem('isAdmin') === 'false') {
      this.assessmentService
        .getActiveAssessmentsForCurrentAdmin(
          sessionStorage.getItem('adminId') ?? ''
        )
        .subscribe({
          next: (data: ActiveAssessmentsResponse) => {
            if (data.statusMessage === 'SUCCESS' && data.response) {
              this.activeAssessments = data.response;
            } else {
              this.activeAssessments = [];
              this.errorMessage =
                data.statusMessage || 'Failed to load active assessments.';
            }
            this.isLoading = false;
          },
          error: (err) => {
            this.isLoading = false;
            this.activeAssessments = [];
            this.adminService.onSessionTimeout();
          },
        });
      return;
    }

    this.assessmentService.getActiveAssessments().subscribe({
      next: (data: ActiveAssessmentsResponse) => {
        if (data.statusMessage === 'SUCCESS' && data.response) {
          this.activeAssessments = data.response;
        } else {
          this.activeAssessments = [];
          this.errorMessage =
            data.statusMessage || 'Failed to load active assessments.';
        }
        this.isLoading = false;
      },
      error: (err) => {
        this.activeAssessments = [];
        this.adminService.onSessionTimeout();
      },
    });
  }

  copyUrl(candidateId: string): void {
    const match = candidateId.match(/\(([^)]+)\)/);
    const finalCandidateId = match ? match[1] : candidateId;

    if (finalCandidateId !== null) {
      this.assessmentService.getUrlByCandidateId(finalCandidateId).subscribe({
        next: (response: UrlRes) => {
          if (response) {
            navigator.clipboard
              .writeText(response.response)
              .then(() => {
                this.copySuccess = true;
                alert('URL copied to clipboard!');
                setTimeout(() => {
                  this.copySuccess = false;
                }, 3000);
              })
              .catch((err) => {
                this.copySuccess = false;
                alert('Failed to copy URL. Please try again or copy manually.');
              });
          } else {
            this.copySuccess = false;
            alert('Could not retrieve a valid URL. Please try again.');
          }
        },
        error: (err) => {
          console.error('Error fetching assessment URL:', err);
          this.copySuccess = false;
          alert(
            'Error fetching assessment URL. Please check your connection or try again later.'
          );
        },
      });
    } else {
      this.copySuccess = false;
    }
  }

  extendAssessment(candidateId: string): void {
    const match = candidateId.match(/\(([^)]+)\)/);
    const extractedId = match ? match[1] : null;

    if (extractedId !== null) {
      candidateId = extractedId;
    }
    const dialogRef = this.dialog.open(ExtendAssessmentDialogComponent, {
      width: '350px',
      data: { message: 'Enter minutes to extend:' },
    });

    dialogRef.afterClosed().subscribe((minutes) => {
      if (minutes !== null && minutes > 0) {
        const extendAssessmentObj: ExtendAssessment = {
          candidateId: candidateId,
          minutes: minutes,
        };

        this.assessmentService.extendAssessment(extendAssessmentObj).subscribe({
          next: () => {
            this.fetchActiveAssessments();
          },
          error: (err) => {
            this.adminService.onSessionTimeout();
          },
        });
      } else if (minutes === null) {
        console.log('Extension cancelled or invalid input.');
      }
      this.fetchActiveAssessments();
    });
  }

  endAssessment(candidateId: string): void {
    const match = candidateId.match(/\(([^)]+)\)/);
    const extractedId = match ? match[1] : null;

    if (extractedId !== null) {
      candidateId = extractedId;
    }

    this.openConfirmationDialogBox(2).then((flag: boolean) => {
      if (flag) {
        this.assessmentService.endAssessmentByAdmin(candidateId).subscribe({
          next: () => {
            this.fetchActiveAssessments();
          },
          error: (err) => {
            console.error('Error ending assessment:', err);
            alert('Failed to end assessment.');
          },
        });
      }
    });
  }

  openConfirmationDialogBox(action: number): Promise<boolean> {
    const message =
      action === 1
        ? 'Do you really want to extend the Assessment?'
        : 'Do you really want to end the Assessment?';

    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: message },
    });

    this.fetchActiveAssessments();

    return dialogRef
      .afterClosed()
      .toPromise()
      .then((result) => {
        return result === true;
      });
  }

  goBack() {
    this.router.navigate(['/admin-panel']);
  }
}
