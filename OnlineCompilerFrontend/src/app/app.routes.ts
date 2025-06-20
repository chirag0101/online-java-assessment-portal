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

export const routes: Routes = [
  { path: '', redirectTo: 'admin-login', pathMatch: 'full' },
  {
    path: 'admin-panel',
    component: AdminPanelComponent,
    canActivate: [AdminAuthGuard],
  },
  { path: 'admin-login', component: AdminLoginComponent },
  { path: 'reset-password', component: ResetAdminPasswordComponent },
  {
    path: 'assessment',
    component: AssessmentHomepageComponent,
    canActivate: [AssessmentEndedGuard],
  },
  {
    path: 'new-assessment',
    component: NewAssessmentComponent,
    canActivate: [AdminAuthGuard],
  },
  {
    path: 'view-active-assessments',
    component: ViewActiveAssessmentsComponent,
    canActivate: [AdminAuthGuard],
  },
  {
    path: 'view-submissions',
    component: ViewSubmissionsComponent,
    canActivate: [AdminAuthGuard],
  },
  {
    path: 'new-admin',
    component: NewAdminComponent,
    canActivate: [AdminAuthGuard],
  },
  { path: 'thank-you', component: ThankYouComponent },
  {
    path: 'code-info',
    component: CodeSubmissionInfoComponent,
    canActivate: [AdminAuthGuard],
  },
];
