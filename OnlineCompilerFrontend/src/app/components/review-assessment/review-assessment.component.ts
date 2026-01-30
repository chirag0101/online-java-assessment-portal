import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AssessmentService } from '../../services/assessment.service';
import { Router } from '@angular/router';
import { ReviewStatus } from '../../models/constants/review-assessment.constants';
import { MatIconModule } from '@angular/material/icon';
import { AdminService } from '../../services/admin.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog'; // Import MatDialogModule
import { AssessmentHistory } from '../../models/assessment-history.model';
import { ReviewAssessmentDialogComponent } from '../review-assessment-dialog/review-assessment-dialog.component';
import { ViewReportRes } from '../../models/response/view-report-res.model';
import { NavbarText } from '../../models/constants/navbar-text.constants';
import { ViewReportReq } from '../../models/request/view-report-req.model';

@Component({
  selector: 'app-review-assessment',
  templateUrl: './review-assessment.component.html',
  styleUrl: './review-assessment.component.css',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    HttpClientModule, 
    MatIconModule, 
    MatDialogModule
  ]
})
export class ReviewAssessmentComponent implements OnInit {
  reviewStatusEnum = ReviewStatus;
  allSubmissions: AssessmentHistory[] = [];
  filteredSubmissions: AssessmentHistory[] = [];
  globallyFilteredSubmissions: AssessmentHistory[] = [];

    version=NavbarText.Version;
    copyright=NavbarText.Copyright;
    portalTitle=NavbarText.PortalName;

  fromDate: string = '';
  toDate: string = '';
  globalFilterText: string = '';
  loading: boolean = false;
  error: boolean = false;
  todayDate: string;
  adminId=sessionStorage.getItem("adminFullName");
  lastLoggedIn=sessionStorage.getItem("lastLoggedIn");

  viewSubmissionReq:ViewReportReq={
  candidateId: '',
  interviewerId: '',
  round: '',
  assessmentEndTime: '',
  }

  // Inject MatDialog in the constructor
  constructor(
    private router: Router, 
    private assessmentService: AssessmentService,
    private adminService: AdminService,
    private dialog: MatDialog 
  ) {
    const today = new Date();
    this.todayDate = today.toISOString().split('T')[0];
  }

  ngOnInit(): void {
    this.fetchSubmissions();
  }

  fetchSubmissions(): void {
    this.loading = true;
    this.error = false;
    const adminId: string = sessionStorage.getItem('adminId') ?? '';

    this.assessmentService.getCandidatesStatusByAdminId(adminId).subscribe({
      next: (res) => {
        if (res && res.response) {
          this.allSubmissions = res.response;
          this.allSubmissions.sort((a, b) => new Date(b.assessmentEndTime).getTime() - new Date(a.assessmentEndTime).getTime());
          this.filteredSubmissions = [...this.allSubmissions];
          this.applyGlobalFilter();
        }
        this.loading = false;
      },
      error: (err) => {
        this.error = true;
        this.loading = false;
        this.adminService.onSessionTimeout();
      },
    });
  }

viewReport(candidateId: string,interviewerId:string,assessmentEndTime:string,round:string): void {
  debugger;
  this.loading = true;

  this.viewSubmissionReq.candidateId=candidateId;
  this.viewSubmissionReq.interviewerId=interviewerId;
  this.viewSubmissionReq.assessmentEndTime=assessmentEndTime;
  this.viewSubmissionReq.round=round;

  this.assessmentService.getCandidateReport(this.viewSubmissionReq).subscribe({
    next: (res: ViewReportRes) => {
      this.loading = false;
      
      if (res && res.status && res.response) {
        const dialogRef = this.dialog.open(ReviewAssessmentDialogComponent, {
          width: '80%',
          maxWidth: '95vw',
          data: res.response
        });

        dialogRef.afterClosed().subscribe(result => {
          if (result && result.action) {
            console.log(`Assessment ${result.action}. Refreshing list...`);
            this.fetchSubmissions(); 
          }
        });
      } else {
        alert(res.statusMessage || 'Failed to fetch report data.');
      }
    },
    error: (err) => {
      this.loading = false;
      this.adminService.onSessionTimeout();
    }
  });
}

filterSubmissionsByDate(): void {
    const fromDateObj = this.fromDate ? new Date(this.fromDate) : null;
    const toDateObj = this.toDate ? new Date(this.toDate) : null;

    this.filteredSubmissions = this.allSubmissions.filter((submission) => {
      const submissionTime = new Date(submission.assessmentEndTime);
      const startOfDayFrom = fromDateObj ? new Date(fromDateObj.getFullYear(), fromDateObj.getMonth(), fromDateObj.getDate(), 0, 0, 0, 0) : null;
      const endOfDayTo = toDateObj ? new Date(toDateObj.getFullYear(), toDateObj.getMonth(), toDateObj.getDate(), 23, 59, 59, 999) : null;
      return (startOfDayFrom ? submissionTime >= startOfDayFrom : true) && (endOfDayTo ? submissionTime <= endOfDayTo : true);
    });
    this.applyGlobalFilter();
  }

  applyGlobalFilter(): void {
    const filterText = this.globalFilterText.toLowerCase().trim();
    if (!filterText) {
      this.globallyFilteredSubmissions = [...this.filteredSubmissions];
      return;
    }
    this.globallyFilteredSubmissions = this.filteredSubmissions.filter(submission => {
      const searchableString = `${submission.candidateName} ${submission.candidateId} ${submission.status}`.toLowerCase();
      return searchableString.includes(filterText);
    });
  }

  clearDateFilter(): void { this.fromDate = ''; this.toDate = ''; this.filteredSubmissions = [...this.allSubmissions]; this.applyGlobalFilter(); }
  clearGlobalFilter(): void { this.globalFilterText = ''; this.applyGlobalFilter(); }
  goBack(): void { this.router.navigate(['/adminPanel']); }

  logOut():void{
    this.adminService.logoutAdmin();
    this.router.navigate(['/adminLogin']);
  }
}