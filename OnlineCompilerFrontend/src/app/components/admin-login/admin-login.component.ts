import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminService } from '../../services/admin.service';
import { AdminLoginCreds } from '../../models/admin-login-creds.model';
import { StatusConstants } from '../../models/constants/status.constants';
import { NavbarText } from '../../models/constants/navbar-text.constants';

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

  portalName:string=NavbarText.PortalName;
  copyright:string=NavbarText.Copyright;
  version:string=NavbarText.Version;

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
        if (response.status) {
          this.adminId = response.response.adminId;
          this.adminService.setIsLoggedIn(true);
          sessionStorage.setItem("accessToken",response.response.accessToken);
          sessionStorage.setItem("lastLoggedIn",response.response.lastLoggedIn);
          this.router.navigate(['/adminPanel']);
        } else {
          this.errorMessage = response.statusMessage;
          this.adminService.setIsLoggedIn(false);
        }
      },
      error: (err) => {
        this.errorMessage = err.statusMessage|| "Login Failed!";
      },
    });
  }

  resetPassword():void{
    this.router.navigate(['/resetPassword']);
  }
}
