import { Component, OnInit, ViewChild } from '@angular/core';

import { CommonModule } from '@angular/common';

import { FormsModule, NgForm } from '@angular/forms';

import { MatDialog, MatDialogModule } from '@angular/material/dialog';

import { Router } from '@angular/router';

import { MatIconModule } from '@angular/material/icon';

import { MatDatepickerModule } from '@angular/material/datepicker';

import { MatNativeDateModule } from '@angular/material/core';

import { MatInputModule } from '@angular/material/input';

import { MatAutocompleteModule } from '@angular/material/autocomplete'; // Added for search

import { DropdownModule } from 'primeng/dropdown';

import { CandidateSearchDialogComponent } from '../../components/candidate-search-dialog/candidate-search-dialog.component';

import {
  AssessmentService,
  CandidateDetialsReq,
} from '../../services/assessment.service';

import { CandidateDetails } from '../../models/candidate-details.model';

import { AdminService } from '../../services/admin.service';

import {
  AdminDetail,
  AdminDetailsResponse,
} from '../../models/response/all-admins-res.model';
import { NavbarText } from '../../models/constants/navbar-text.constants';

@Component({
  selector: 'app-new-assessment',

  standalone: true,

  imports: [
    CommonModule,

    FormsModule,

    MatIconModule,

    MatDialogModule,

    MatDatepickerModule,

    MatNativeDateModule,

    MatInputModule,

    MatAutocompleteModule, // Added

    DropdownModule,
  ],

  templateUrl: './new-assessment.component.html',

  styleUrl: './new-assessment.component.css',
})
export class NewAssessmentComponent implements OnInit {
  @ViewChild('assessmentForm') assessmentForm!: NgForm;

  adminId: string | null | undefined;

  adminFullName: string | null | undefined;

  lastLoggedIn = sessionStorage.getItem('lastLoggedIn');

  isAdmin: string | null | undefined;

  interviewerId: string = '';

  interviewerSearchText: string = ''; // New: For the search input text

  candidateFullName: string = '';

  candidateEmail: string = '';

  technology: string = 'JAVA';

  candidateId: string = '';

  yearsOfExperience: number | null = null;

  interviewRound: string = '';

  selectedDate: Date | null = null;

  selectedTime: string = '';

  isInterviewerIdDisabled: boolean = false;

  isCandidateDetailsLoaded: boolean = false;

  assessmentCreatedMessage: string = '';

  assessmentUrl: string = '';

  copySuccess: boolean = false;

  interviewRounds: string[] = ['R1', 'R2'];

  filteredRounds: string[] = [];

  allAdmins: AdminDetail[] = [];

  filteredAdmins: AdminDetail[] = []; // New: Filtered list for dropdown

  message: string = '';

  isSuccess: boolean = false;

  minDate: Date = new Date();

  candidateDetialsReq: CandidateDetialsReq = {
    candidateId: '',

    interviewerId: '',

    candidateFullName: '',

    candidateEmailId: '',

    candidateTechnology: '',

    candidateYearsOfExpInMonths: 0,

    adminId: '',

    interviewRound: '',

    interviewDateTime: '',
  };

    version=NavbarText.Version;
    copyright=NavbarText.Copyright;
    portalTitle=NavbarText.PortalName;

  constructor(
    private dialog: MatDialog,

    private router: Router,

    private assessmentService: AssessmentService,

    private adminService: AdminService,
  ) {}

  ngOnInit() {
    this.isAdmin = sessionStorage.getItem('isAdmin');

    this.adminId = sessionStorage.getItem('adminId');

    this.minDate = new Date();

    this.filteredRounds = [...this.interviewRounds];

    if (this.isAdmin === 'false') {
      this.adminFullName = sessionStorage.getItem('adminFullName');

      this.interviewerId = this.adminId ?? '';

      this.isInterviewerIdDisabled = true;
    } else {
      this.getAllAdmins();
    }
  }

  getAllAdmins(): void {
    this.adminService.getAllAdmins().subscribe({
      next: (res: AdminDetailsResponse) => {
        if (res.status && res.response) {
          this.allAdmins = res.response.map((item: AdminDetail) => ({
            adminFullName: item.adminFullName,

            adminId: item.adminId,
          }));

          this.filteredAdmins = [...this.allAdmins];
        } else {
          this.router.navigate(['/adminLogin']);
        }
      },

      error: () => this.adminService.onSessionTimeout(),
    });
  }

  // New: Filter logic for search

  filterAdmins(event: any): void {
    const query = (event.target.value || '').toLowerCase();

    this.filteredAdmins = this.allAdmins.filter((admin) =>
      admin.adminFullName.toLowerCase().includes(query),
    );

    if (!query) this.interviewerId = '';
  }

  // New: Selection logic

  onInterviewerSelected(event: any): void {
    const selectedName = event.option.value;

    const selectedAdmin = this.allAdmins.find(
      (a) => a.adminFullName === selectedName,
    );

    if (selectedAdmin) {
      this.interviewerId = selectedAdmin.adminId;

      this.interviewerSearchText = selectedAdmin.adminFullName;
    }
  }

  onRoundSelected(event: any): void {
    this.interviewRound = event.option.value;
  }

  filterRounds(event: any): void {
    const query = event.target.value.toLowerCase();

    this.filteredRounds = this.interviewRounds.filter((r) =>
      r.toLowerCase().includes(query),
    );
  }

  openCandidateSearch(): void {
    const dialogRef = this.dialog.open(CandidateSearchDialogComponent, {
      width: '80%',

      maxWidth: '95vw',

      data: { message: 'Select an existing candidate or input details.' },
    });

    dialogRef
      .afterClosed()
      .subscribe((selectedCandidate: CandidateDetails | null) => {
        if (selectedCandidate) {
          this.candidateId = selectedCandidate.candidateId;

          this.candidateFullName = selectedCandidate.candidateFullName;

          this.candidateEmail = selectedCandidate.candidateEmailId;

          this.technology = selectedCandidate.candidateTechnology;

          this.yearsOfExperience =
            selectedCandidate.candidateYearsOfExpInMonths;

          this.isCandidateDetailsLoaded = true;
        }
      });
  }

  fetchCandidateDetailsByEmail(): void {
    if (!this.candidateEmail) {
      this.resetCandidateFields();

      return;
    }

    this.assessmentService
      .getCandidateDetByEmail(this.candidateEmail)
      .subscribe({
        next: (response) => {
          if (response.status) {
            this.candidateId = response.response.candidateId;

            this.candidateFullName = response.response.candidateFullName;

            this.technology = response.response.candidateTechnology;

            this.yearsOfExperience =
              response.response.candidateYearsOfExpInMonths;

            this.isCandidateDetailsLoaded = true;
          } else {
            this.resetCandidateFields();
          }
        },

        error: () => {
          this.isCandidateDetailsLoaded = false;

          this.adminService.onSessionTimeout();
        },
      });
  }

  private resetCandidateFields(): void {
    this.isCandidateDetailsLoaded = false;

    this.candidateId = '';

    this.candidateFullName = '';

    this.yearsOfExperience = null;
  }

  createAssessment(form: NgForm): void {
    form.control.markAllAsTouched();

    if (
      form.invalid ||
      !this.selectedDate ||
      !this.selectedTime ||
      !this.interviewerId
    ) {
      this.message = 'Please complete all required fields.';

      this.isSuccess = false;

      return;
    }

    const [hours, minutes] = this.selectedTime.split(':');

    const scheduledDateTime = new Date(this.selectedDate);

    scheduledDateTime.setHours(parseInt(hours), parseInt(minutes));

    this.candidateDetialsReq = {
      candidateId: this.candidateId,

      candidateEmailId: this.candidateEmail,

      candidateFullName: this.candidateFullName,

      candidateTechnology: this.technology,

      candidateYearsOfExpInMonths: this.yearsOfExperience ?? 0,

      interviewerId: this.interviewerId,

      interviewRound: this.interviewRound,

      adminId: sessionStorage.getItem('adminId') ?? '',

      interviewDateTime: scheduledDateTime.toISOString(),
    };

    this.assessmentService
      .createAssessment(this.candidateDetialsReq)
      .subscribe({
        next: (response) => {
          if (response.status == true) {
            this.assessmentUrl = response.response;

            this.message = response.statusMessage;

            this.isSuccess = true;
          } else {
            this.isSuccess = false;

            this.message = response.statusMessage;
          }
        },

        error: () => {
          this.adminService.onSessionTimeout();
        },
      });
  }

  resetForm(): void {
    if (this.assessmentForm) {
      this.assessmentForm.resetForm();
    }

    this.interviewerSearchText = '';

    this.interviewerId = this.isInterviewerIdDisabled
      ? (this.adminId ?? '')
      : '';

    this.candidateFullName = '';

    this.candidateEmail = '';

    this.technology = 'JAVA';

    this.candidateId = '';

    this.yearsOfExperience = null;

    this.interviewRound = '';

    this.selectedDate = null;

    this.selectedTime = '';

    this.assessmentUrl = '';

    this.isCandidateDetailsLoaded = false;
  }

  copyUrl(): void {
    if (this.assessmentUrl) {
      navigator.clipboard.writeText(this.assessmentUrl).then(() => {
        this.copySuccess = true;

        setTimeout(() => (this.copySuccess = false), 3000);
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/adminPanel']);
  }

  logOut(): void {
    this.adminService.logoutAdmin();
    this.router.navigate(['/adminLogin']);
  }
}
