import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { CandidateSearchDialogComponent } from '../../components/candidate-search-dialog/candidate-search-dialog.component';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import {
  AssessmentService,
  CandidateDetialsReq,
} from '../../services/assessment.service';
import { CandidateDetails } from '../../models/candidate-details.model';
import { AdminService } from '../../services/admin.service';
import {
  AdminDetail,
  AdminDetailsResponse,
} from '../../models/all-admins-res.model';
import { Dropdown, DropdownModule } from 'primeng/dropdown';
@Component({
  selector: 'app-new-assessment',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule, DropdownModule],
  templateUrl: './new-assessment.component.html',
  styleUrl: './new-assessment.component.css',
})
export class NewAssessmentComponent implements OnInit {
  @ViewChild('assessmentForm') assessmentForm!: NgForm;

  interviewerId: string = '';
  interviewerFullName: string = '';

  candidateFullName: string = '';
  candidateEmail: string = '';
  technology: string = 'JAVA';
  candidateId: string = '';
  yearsOfExperience: number | null = null;

  isAdmin: string | null | undefined;
  adminId: string | null | undefined;
  isInterviewerIdDisabled: boolean = false;
  isCandidateDetailsLoaded: boolean = false;
  adminFullName: string | null | undefined;
  assessmentCreatedMessage: string = '';
  assessmentUrl: string = '';
  copySuccess: boolean = false;

  interviewRound: string = '';

  //interview rounds
  interviewRounds: string[] = ['R1', 'R2'];

  allAdmins: AdminDetail[] = [];

  constructor(
    private dialog: MatDialog,
    private router: Router,
    private assessmentService: AssessmentService,
    private adminService: AdminService
  ) {}

  ngOnInit() {
    this.isAdmin = sessionStorage.getItem('isAdmin');
    this.adminId = sessionStorage.getItem('adminId');
    if (this.isAdmin == 'false') {
      this.adminFullName = sessionStorage.getItem('adminFullName');
      this.interviewerId = this.adminId ?? '';
      this.interviewerFullName = this.adminFullName ?? '';
      this.isInterviewerIdDisabled = true;
    } else {
      this.getAllAdmins();
    }
  }

  openCandidateSearch(): void {
    const dialogRef = this.dialog.open(CandidateSearchDialogComponent, {
      width: '800px',
      data: { message: 'Select an existing candidate or input details.' },
    });

    dialogRef
      .afterClosed()
      .subscribe((selectedCandidate: CandidateDetails | null) => {
        if (selectedCandidate) {
          debugger;
          this.candidateId = selectedCandidate.candidateId;
          this.candidateFullName = selectedCandidate.candidateFullName;
          this.candidateEmail = selectedCandidate.candidateEmailId;
          this.technology = selectedCandidate.candidateTechnology;
          this.yearsOfExperience =
            selectedCandidate.candidateYearsOfExpInMonths;
          this.isCandidateDetailsLoaded = true;
          if (
            this.assessmentForm &&
            this.assessmentForm.controls['candidateId']
          ) {
            this.assessmentForm.controls['candidateId'].markAsDirty();
            this.assessmentForm.controls['candidateId'].markAsTouched();
            this.assessmentForm.controls[
              'candidateId'
            ].updateValueAndValidity();
          }
        }
      });
  }

  candidateDetialsReq: CandidateDetialsReq = {
    candidateId: this.candidateId,
    interviewerId: this.interviewerId,
    candidateFullName: this.candidateFullName,
    candidateEmailId: this.candidateEmail,
    candidateTechnology: this.technology,
    candidateYearsOfExpInMonths: this.yearsOfExperience ?? 0,
    adminId: sessionStorage.getItem('adminId') ?? '',
    interviewRound: this.interviewRound,
  };

  createAssessment(form: NgForm): void {
    debugger;
    form.control.markAllAsTouched();

    if (form.invalid) {
      this.assessmentCreatedMessage = 'Please correct the errors in the form.';
      this.assessmentUrl = '';
      return;
    }
    this.candidateDetialsReq.candidateId = this.candidateId;
    this.candidateDetialsReq.candidateEmailId = this.candidateEmail;
    this.candidateDetialsReq.candidateFullName = this.candidateFullName;
    this.candidateDetialsReq.candidateTechnology = this.technology;
    this.candidateDetialsReq.candidateYearsOfExpInMonths = this.yearsOfExperience;
    this.candidateDetialsReq.interviewerId = this.interviewerId;
    this.candidateDetialsReq.interviewRound = this.interviewRound;

    this.assessmentService
      .createAssessment(this.candidateDetialsReq)
      .subscribe({
        next: (response) => {
          this.assessmentUrl = response.response;
          alert('Candidate created & URL created!');
        },
        error: (err) => {
          sessionStorage.clear();
          alert('Failed to create assessment.');
          this.router.navigate(['/admin-login']);
        },
      });

    this.assessmentUrl = ``;
    this.copySuccess = false;
  }

  resetForm(): void {
    if (this.assessmentForm) {
      this.assessmentForm.resetForm();
    }
    this.interviewerId = '';
    this.candidateFullName = '';
    (this.candidateEmail = ''), (this.technology = '');
    this.candidateId = '';
    this.yearsOfExperience = null;
    this.assessmentCreatedMessage = '';
    this.assessmentUrl = '';
    this.copySuccess = false;
    this.isCandidateDetailsLoaded = false;
  }

  copyUrl(): void {
    if (this.assessmentUrl) {
      navigator.clipboard
        .writeText(this.assessmentUrl)
        .then(() => {
          this.copySuccess = true;
          setTimeout(() => {
            this.copySuccess = false;
          }, 3000);
        })
        .catch((err) => {
          console.error('Failed to copy URL: ', err);
          this.copySuccess = false;
          alert('Failed to copy URL. Please try again or copy manually.');
        });
    }
  }

  fetchCandidateDetailsByEmail(): void {
    if (!this.candidateEmail) {
      this.isCandidateDetailsLoaded = false;
      this.candidateId = '';
      this.candidateFullName = '';
      this.yearsOfExperience = null;
      return;
    }

    this.assessmentService
      .getCandidateDetByEmail(this.candidateEmail).subscribe({
        next: (response) => {
          debugger;
          if (response.status) {
            this.candidateId = response.response.candidateId;
            this.candidateFullName = response.response.candidateFullName;
            this.technology = response.response.candidateTechnology;
            this.isCandidateDetailsLoaded = true;
          } else {
            this.isCandidateDetailsLoaded = false;
            this.candidateId = '';
            this.candidateFullName = '';
            this.yearsOfExperience = null;
          }
        },
        error: (err) => {
          this.isCandidateDetailsLoaded = false;
          alert("Session Expired!");
          this.router.navigate(['/admin-login']);
        },
      });
  }

  getAllAdmins(): void {
    this.adminService.getAllAdmins().subscribe({
      next: (res: AdminDetailsResponse) => {
        if (res.status && res.response) {
          this.allAdmins = res.response.map((item: AdminDetail) => ({
            adminFullName: item.adminFullName,
            adminId: item.adminId,
          }));
        } else {
          this.router.navigate(['/admin-login']);
        }
      },
      error: (err: any) => {
        debugger;
        alert("Session Expired!");
        this.router.navigate(['/admin-login']);
      },
    });
  }

  goBack(): void {
    this.router.navigate(['/admin-panel']);
  }
}
