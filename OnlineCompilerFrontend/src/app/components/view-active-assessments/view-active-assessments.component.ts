import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import {
  AssessmentService,
  ActiveAssessment,
  ActiveAssessmentsResponse,
  ExtendAssessment,
  UrlRes,
} from '../../services/assessment.service';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { ExtendAssessmentDialogComponent } from '../extend-assessment-dialog/extend-assessment-dialog.component';
import { MatIconModule } from '@angular/material/icon';
import { AdminService } from '../../services/admin.service';
import { ExpireAssessmentReq } from '../../models/request/expire-assessment-req-model';
import { NavbarText } from '../../models/constants/navbar-text.constants';

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

  adminId = sessionStorage.getItem('adminFullName');
  lastLoggedIn = sessionStorage.getItem('lastLoggedIn');
  req: ExpireAssessmentReq = {
    candidateId: '',
    assessmentUrl: '',
    interviewRound: '',
  };
  version = NavbarText.Version;
  copyright = NavbarText.Copyright;
  portalTitle = NavbarText.PortalName;

  constructor(
    private assessmentService: AssessmentService,
    private router: Router,
    private dialog: MatDialog,
    private adminService: AdminService,
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
          sessionStorage.getItem('adminId') ?? '',
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
            debugger;
            this.isLoading = false;
            this.activeAssessments = [];
            this.errorMessage=this.adminService.onError(err);
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
        this.isLoading = false;
        this.activeAssessments = [];
        this.errorMessage=this.adminService.onError(err);
      },
    });
  }

  copyUrl(url: string) {
    if (url) {
      navigator.clipboard.writeText(url).then(() => {
        this.copySuccess = true;
        alert('Assessment URL copied to clipboard!');
        setTimeout(() => (this.copySuccess = false), 2000);
      });
    }
  }
  extendAssessment(candidateId: string, interviewRound: string): void {
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
          interviewRound: interviewRound,
        };

        this.assessmentService.extendAssessment(extendAssessmentObj).subscribe({
          next: () => {
            this.fetchActiveAssessments();
          },
          error: (error) => {
            this.errorMessage = this.adminService.onError(error);
          },
        });
      } else if (minutes === null) {
        console.log('Extension cancelled or invalid input.');
      }
      this.fetchActiveAssessments();
    });
  }

  endAssessment(
    candidateId: string,
    assessmentUrl: string,
    round: string,
  ): void {
    const match = candidateId.match(/\(([^)]+)\)/);
    const extractedId = match ? match[1] : null;

    if (extractedId !== null) {
      candidateId = extractedId;
    }
    this.req.candidateId = candidateId;
    this.req.assessmentUrl = assessmentUrl;
    this.req.interviewRound = round;

    this.openConfirmationDialogBox(2).then((flag: boolean) => {
      if (flag) {
        this.assessmentService.endAssessmentByAdmin(this.req).subscribe({
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
    this.router.navigate(['/adminPanel']);
  }

  logOut(): void {
    this.adminService.logoutAdmin();
    this.router.navigate(['/adminLogin']);
  }
}
