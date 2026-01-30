import { RouterModule, Routes } from '@angular/router';
import { AssessmentHomepageComponent } from './components/assessment-homepage/assessment-homepage.component';
import { AdminPanelComponent } from './components/admin-panel/admin-panel.component';
import { ThankYouComponent } from './components/thank-you/thank-you.component';
import { AdminLoginComponent } from './components/admin-login/admin-login.component';
import { ViewSubmissionsComponent } from './components/view-submissions/view-submissions.component';
import { NewAdminComponent } from './components/new-admin/new-admin.component';
import { NewAssessmentComponent } from './components/new-assessment/new-assessment.component';
import { ViewActiveAssessmentsComponent } from './components/view-active-assessments/view-active-assessments.component';
import { AdminAuthGuard } from './guards/admin-auth.guard';
import { CodeSubmissionInfoComponent } from './components/code-submission-info/code-submission-info.component';
import { AssessmentEndedGuard } from './guards/assessment-end.guard';
import { ResetAdminPasswordComponent } from './components/reset-admin-password/reset-admin-password.component';
import { SqlEditorComponent } from './components/sql-editor/sql-editor.component';
import { JsonEditorComponent } from './components/json-editor/json-editor.component';
import { TextEditorComponent } from './components/text-editor/text-editor.component';
import { ReviewAssessmentComponent } from './components/review-assessment/review-assessment.component';

export const routes: Routes = [
  { path: '', redirectTo: 'adminLogin', pathMatch: 'full' },
  {
    path: 'adminPanel',
    component: AdminPanelComponent,
    canActivate: [AdminAuthGuard],
  },
  { path: 'adminLogin', component: AdminLoginComponent },
  { path: 'resetPassword', component: ResetAdminPasswordComponent },
  {
    path: 'assessment',
    component: AssessmentHomepageComponent,
    canActivate: [AssessmentEndedGuard],
  },
  {
    path: 'newAssessment',
    component: NewAssessmentComponent,
    canActivate: [AdminAuthGuard],
  },
  {
    path: 'viewActiveAssessments',
    component: ViewActiveAssessmentsComponent,
    canActivate: [AdminAuthGuard],
  },
  {
    path: 'viewSubmissions',
    component: ViewSubmissionsComponent,
    canActivate: [AdminAuthGuard],
  },
  {
    path: 'newAdmin',
    component: NewAdminComponent,
    canActivate: [AdminAuthGuard],
  },
  { path: 'thankYou', component: ThankYouComponent },
  {
    path: 'codeInfo',
    component: CodeSubmissionInfoComponent,
    canActivate: [AdminAuthGuard],
  },
  {
    path: 'reviewAssessment',
    component: ReviewAssessmentComponent,
    canActivate: [AdminAuthGuard],
  },
  {
    path: 'sqlEditor',
    component: SqlEditorComponent
  },
  {
    path: 'jsonEditor',
    component: JsonEditorComponent
  },
  {
    path: 'textEditor',
    component: TextEditorComponent
  }
];
