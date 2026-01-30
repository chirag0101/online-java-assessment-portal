import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AssessmentService } from '../../services/assessment.service';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { AdminService } from '../../services/admin.service';
import { SubmissionDetails } from '../../models/submission-details.model';
import {
  ApiResponse,
  CandidateSubmissionResponse,
  GetCandidateSubmissionsRes,
} from '../../models/response/candidate-submissions-res.model';
import { NavbarText } from '../../models/constants/navbar-text.constants';

@Component({
  selector: 'app-view-submissions',
  templateUrl: './view-submissions.component.html',
  styleUrls: ['./view-submissions.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, MatIconModule],
})
export class ViewSubmissionsComponent implements OnInit {
  allSubmissions: SubmissionDetails[] = [];
  filteredSubmissions: SubmissionDetails[] = [];
  globallyFilteredSubmissions: SubmissionDetails[] = [];

  fromDate: string = '';
  toDate: string = '';
  globalFilterText: string = '';

  loading: boolean = false;
  error: boolean = false;
  todayDate: string;

  savedCandidateId: string = '';
  savedLangType: string = '';
  candidateName: string = '';
  savedInterviewerId: string = '';
  savedRound: string = '';
  savedAssessmentEndTime: string = '';

  version = NavbarText.Version;
  copyright = NavbarText.Copyright;
  portalTitle = NavbarText.PortalName;

  adminId = sessionStorage.getItem('adminFullName');
  lastLoggedIn = sessionStorage.getItem('lastLoggedIn');

  constructor(
    private router: Router,
    private assessmentService: AssessmentService,
    private adminService: AdminService,
  ) {
    const today = new Date();
    this.todayDate = today.toISOString().split('T')[0];

    const navigation = this.router.getCurrentNavigation();
    const state = navigation?.extras.state as {
      tech: string;
      candidate: string;
      name?: string;
      assessmentEndTime:string;
      round:string;
      interviewer:string;
    };

    if (state) {
      this.savedCandidateId = state.candidate;
      this.savedLangType = state.tech;
      this.candidateName = state.name || 'N/A';
      this.savedAssessmentEndTime = state.assessmentEndTime;
      this.savedRound=state.round
      this.savedInterviewerId=state.interviewer;
    }
  }

  ngOnInit(): void {
    if (this.savedCandidateId && this.savedLangType) {
      this.fetchSubmissions();
    } else {
      this.router.navigate(['/adminPanel']);
    }
  }

  fetchSubmissions(): void {
    this.loading = true;
    this.error = false;

    const req = {
      candidateId: this.savedCandidateId,
      langType: this.savedLangType,
      interviewerId: this.savedInterviewerId,
      round: this.savedRound,
      assessmentEndTime: this.savedAssessmentEndTime,
    };

    this.assessmentService.getCandidateSubmissionsByCandidateId(req).subscribe({
      next: (res: ApiResponse) => {
        if (res && res.status && res.response && res.response.codeSubmissions) {
          this.allSubmissions = res.response.codeSubmissions.map(
            (item: GetCandidateSubmissionsRes) => {
              return {
                candidateId: res.response.candidateId,
                candidateFullName: res.response.candidateName,
                interviewerId: res.response.interviewerId,
                codeType: res.response.codeType,
                code: item.code,
                time: item.time,
                actionPerformed: item.action,
                status: item.status === null ? 'N/A' : item.status,
                output: item.output,
                round: item.round === null ? 'N/A' : item.round,
              } as SubmissionDetails;
            },
          );

          this.processIncomingData();
        } else {
          this.allSubmissions = [];
          this.processIncomingData();
        }
        this.loading = false;
      },
      error: (err) => {
        console.error('Fetch error:', err);
        this.error = true;
        this.loading = false;
      },
    });
  }

  private processIncomingData(): void {
    if (this.allSubmissions.length > 0) {
      // Sort by time descending (newest first)
      this.allSubmissions.sort(
        (a, b) => new Date(b.time).getTime() - new Date(a.time).getTime(),
      );
      this.filteredSubmissions = [...this.allSubmissions];
    } else {
      this.filteredSubmissions = [];
    }
    this.applyGlobalFilter();
  }

  applyGlobalFilter(): void {
    const filterText = this.globalFilterText
      ? this.globalFilterText.toLowerCase().trim()
      : '';

    if (!filterText) {
      this.globallyFilteredSubmissions = [...this.filteredSubmissions];
      return;
    }

    this.globallyFilteredSubmissions = this.filteredSubmissions.filter(
      (sub) => {
        const searchStr =
          `${sub.candidateId} ${sub.actionPerformed} ${sub.status} ${sub.code}`.toLowerCase();
        return searchStr.includes(filterText);
      },
    );
  }

  filterSubmissionsByDate(): void {
    const fromDateObj = this.fromDate ? new Date(this.fromDate) : null;
    const toDateObj = this.toDate ? new Date(this.toDate) : null;

    this.filteredSubmissions = this.allSubmissions.filter((sub) => {
      const subTime = new Date(sub.time);
      const start = fromDateObj
        ? new Date(fromDateObj.setHours(0, 0, 0, 0))
        : null;
      const end = toDateObj
        ? new Date(toDateObj.setHours(23, 59, 59, 999))
        : null;

      return (start ? subTime >= start : true) && (end ? subTime <= end : true);
    });

    this.applyGlobalFilter();
  }

  clearDateFilter(): void {
    this.fromDate = '';
    this.toDate = '';
    this.filteredSubmissions = [...this.allSubmissions];
    this.applyGlobalFilter();
  }

  clearGlobalFilter(): void {
    this.globalFilterText = '';
    this.applyGlobalFilter();
  }

  viewCodeDetails(submission: SubmissionDetails): void {
    this.router.navigate(['/codeInfo'], {
      state: { submissionDetails: submission, tech: this.savedLangType, candidateId:this.savedCandidateId, interviewerId:this.savedInterviewerId, assessmentEndTime:this.savedAssessmentEndTime, langType:this.savedLangType, round:this.savedRound },
    });
  }

  goBack(): void {
    this.router.navigate(['/reviewAssessment']);
  }

  logOut(): void {
    this.adminService.logoutAdmin();
    this.router.navigate(['/adminLogin']);
  }
}
