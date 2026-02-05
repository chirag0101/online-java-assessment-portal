import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminService } from '../../services/admin.service';
import { AdminLoginCreds } from '../../models/admin-login-creds.model';
import { StatusConstants } from '../../models/constants/status.constants';
import { OnInit } from '@angular/core';
import { NewAdminDetails } from '../../models/admin-details.model';
import { NavbarText } from '../../models/constants/navbar-text.constants';

export interface NewAdminResponse {
  status: string;
  statusMessage: string;
  response: string;
}

@Component({
  selector: 'app-new-admin-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './new-admin.component.html',
  styleUrls: ['./new-admin.component.css'],
})
export class NewAdminComponent implements OnInit {
  adminId: string = '';
  adminFullName: string = '';
  isAdmin: boolean = false;
  errorMessage: string = '';
  loggedInAdminId = sessionStorage.getItem('adminFullName');
  adminLastLoggedIn = sessionStorage.getItem('lastLoggedIn');
  lastLoggedIn = '';

  portalName: string = NavbarText.PortalName;
  copyright: string = NavbarText.Copyright;
  version: string = NavbarText.Version;

  newAdminDetails: NewAdminDetails = {
    adminId: '',
    adminFullName: '',
    isAdmin: false,
  };

  constructor(
    private router: Router,
    private adminService: AdminService,
  ) {}

  ngOnInit(): void {
    this.adminId = '';
    this.adminFullName = '';
    this.isAdmin = false;
  }

  save(form: NgForm): void {
    form.form.markAllAsTouched();
    if (!this.adminId || !this.adminFullName) {
      this.errorMessage = 'Admin Details are required!';
      return;
    }

    this.newAdminDetails.adminId = this.adminId;
    this.newAdminDetails.adminFullName = this.adminFullName;
    this.newAdminDetails.isAdmin = this.isAdmin;

    this.adminService.newAdmin(this.newAdminDetails).subscribe({
      next: (response) => {
        if (response.status) {
          this.adminId = '';
          alert('Admin Created Successfully!');
          this.router.navigate(['/adminPanel']);
        } else {
          this.errorMessage =
            response.statusMessage || 'Failed to create admin.';
        }
      },
      error: (err) => {
        this.errorMessage =
          this.adminService.onError(err);
      },
    });
  }

  back(): void {
    this.router.navigate(['/adminPanel']);
  }

  logOut(): void {
    this.adminService.logoutAdmin();
    this.router.navigate(['/adminLogin']);
  }
}
