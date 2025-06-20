import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AssessmentService } from '../../services/assessment.service';
import { Router } from '@angular/router';
import { SubmissionDetails } from '../../models/submission-details.model';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-view-submissions',
  templateUrl: './view-submissions.component.html',
  styleUrls: ['./view-submissions.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, MatIconModule],
})
export class ViewSubmissionsComponent implements OnInit {
  allSubmissions: SubmissionDetails[] = []; // Stores the original, full list of submissions
  filteredSubmissions: SubmissionDetails[] = []; // Stores submissions after date filtering
  globallyFilteredSubmissions: SubmissionDetails[] = []; // Stores submissions after both date and global text filtering

  fromDate: string = '';
  toDate: string = '';
  globalFilterText: string = '';

  loading: boolean = false;
  error: boolean = false;
  todayDate: string;

  constructor(private router: Router, private assessmentService: AssessmentService) {
    debugger;
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

    let submissionObservable = sessionStorage.getItem("isAdmin") === "false"
      ? this.assessmentService.getSubmissionsByAdminId(adminId)
      : this.assessmentService.getAllSubmissions();

    submissionObservable.subscribe({
      next: (res) => {
        if (res && res.response) {
          this.allSubmissions = res.response;
          this.allSubmissions.sort((a, b) => new Date(b.time).getTime() - new Date(a.time).getTime());
          this.filteredSubmissions = [...this.allSubmissions];
          this.applyGlobalFilter();
        } else {
          this.allSubmissions = [];
          this.filteredSubmissions = [];
          this.globallyFilteredSubmissions = [];
        }
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching submissions:', err);
        this.error = true;
        this.loading = false;
      },
    });
  }

  viewCodeDetails(submission: SubmissionDetails): void {
    this.router.navigate(['/code-info'], {
      state: { submissionDetails: submission }  //sending the submission object so that it doesn't require an api call
    });
  }

  filterSubmissionsByDate(): void {
    const fromDateObj = this.fromDate ? new Date(this.fromDate) : null;
    const toDateObj = this.toDate ? new Date(this.toDate) : null;

    this.filteredSubmissions = this.allSubmissions.filter((submission) => {
      const submissionTime = new Date(submission.time);

      const startOfDayFrom = fromDateObj ? new Date(fromDateObj.getFullYear(), fromDateObj.getMonth(), fromDateObj.getDate(), 0, 0, 0, 0) : null;
      const endOfDayTo = toDateObj ? new Date(toDateObj.getFullYear(), toDateObj.getMonth(), toDateObj.getDate(), 23, 59, 59, 999) : null;

      const isAfterFrom = startOfDayFrom ? submissionTime >= startOfDayFrom : true;
      const isBeforeTo = endOfDayTo ? submissionTime <= endOfDayTo : true;

      return isAfterFrom && isBeforeTo;
    });

    this.applyGlobalFilter();
  }

  clearDateFilter(): void {
    this.fromDate = '';
    this.toDate = '';
    this.filteredSubmissions = [...this.allSubmissions];
    this.applyGlobalFilter(); 
  }

  applyGlobalFilter(): void {
    const filterText = this.globalFilterText.toLowerCase().trim();

    debugger;

    if (!filterText) {  //if filter contains nothing, storing filteredSubmissions based on dates or storing all the submissions
      this.globallyFilteredSubmissions = [...this.filteredSubmissions];
      return;
    }

    this.globallyFilteredSubmissions = this.filteredSubmissions.filter(submission => {
      const searchableString = `
        ${String(submission.candidateFullName || '').toLowerCase()}
        ${String(submission.candidateId || '').toLowerCase()}
        ${String(submission.interviewerId || '').toLowerCase()}
        ${String(submission.code || '').toLowerCase()}
        ${String(submission.actionPerformed || '').toLowerCase()}
        ${String(submission.status || '').toLowerCase()}
        ${String(submission.output || '').toLowerCase()}
        ${submission.time ? new Date(submission.time).toLocaleDateString().toLowerCase() : ''}
        ${submission.time ? new Date(submission.time).toLocaleTimeString().toLowerCase() : ''}
      `;

      return searchableString.includes(filterText);
    });
  }

  clearGlobalFilter(): void {
    this.globalFilterText = '';
    this.applyGlobalFilter();
  }

  goBack(): void {
    this.router.navigate(['/admin-panel']);
  }
}