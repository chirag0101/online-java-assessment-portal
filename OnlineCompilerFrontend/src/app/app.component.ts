import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AssessmentHomepageComponent } from './components/assessment-homepage/assessment-homepage.component';
import { FormsModule } from '@angular/forms';
import { RouterModule, RouterOutlet } from '@angular/router';
import { AdminPanelComponent } from './components/admin-panel/admin-panel.component';
import { DropdownModule } from 'primeng/dropdown';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    RouterOutlet,
    DropdownModule,
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'My Compiler App';
  currentYear = new Date().getFullYear();
}
