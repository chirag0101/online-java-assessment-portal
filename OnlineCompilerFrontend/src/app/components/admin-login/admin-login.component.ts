import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminService } from '../../services/admin.service';
import { AdminLoginCreds } from '../../models/admin-login-creds.model';
import { StatusConstants } from '../../models/constants/status.constants';

@Component({
  selector: 'app-admin-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-login.component.html',
  styleUrl: './admin-login.component.css',
})
export class AdminLoginComponent implements OnInit{
  adminId: string = '';
  adminPassword: string = '';
  errorMessage: string = '';

  adminLoginCreds: AdminLoginCreds = {
    adminId: '',
    adminPassword: '',
  };

  constructor(private router: Router, private adminService: AdminService) {}

  ngOnInit(): void {
    // sessionStorage.clear();
  }

  onLogin(form: NgForm): void {
    form.form.markAllAsTouched();
    if(!form.valid){
      return;
    }
    if (!this.adminId || !this.adminPassword) {
      this.errorMessage = 'Admin Details are required!';
      return;
    }

    this.adminLoginCreds.adminId = this.adminId;
    this.adminLoginCreds.adminPassword = this.adminPassword;

    this.adminService.authenticateAdmin(this.adminLoginCreds).subscribe({
      next: (response) => {
        debugger;
        if (response.status) {
          this.adminId = response.response.adminId;
          this.adminService.setIsLoggedIn(true);
          sessionStorage.setItem("accessToken",response.response.accessToken);
          this.router.navigate(['/admin-panel']);
        } else {
          this.errorMessage = response.statusMessage;
          this.adminService.setIsLoggedIn(false);
        }
      },
      error: (err) => {
        this.errorMessage = err.statusMessage;
      },
    });
  }

  resetPassword():void{
    this.router.navigate(['/reset-password']);
  }
}
