import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminService } from '../../services/admin.service';
import { AdminLoginCreds } from '../../models/admin-login-creds.model';
import { ResetAdminReq } from '../../models/reset-admin-req.model';

@Component({
  selector: 'reset-admin-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reset-admin-password.component.html',
  styleUrl: './reset-admin-password.component.css',
})
export class ResetAdminPasswordComponent {
  errorMessage: string = '';
  adminId:string='';
  adminOldPassword:string='';
  adminNewPassword:string='';
  adminNewConfirmPassword:string='';

  resetAdminReq: ResetAdminReq = {
    adminId: '',
    adminOldPassword: '',
    adminNewPassword: '',
    adminNewConfirmPassword: '',
  };

  constructor(private router: Router, private adminService: AdminService) {}

  resetPassword(form: NgForm): void {
    form.form.markAllAsTouched();
    if (!this.adminId || !this.adminOldPassword || !this.adminNewPassword || !this.adminNewConfirmPassword) {
      this.errorMessage = 'Admin Details are required!';
      return;
    }

    this.resetAdminReq.adminId=this.adminId;
    this.resetAdminReq.adminOldPassword=this.adminOldPassword;
    this.resetAdminReq.adminNewPassword=this.adminNewPassword;
    this.resetAdminReq.adminNewConfirmPassword=this.adminNewConfirmPassword

    this.adminService.resetPassword(this.resetAdminReq).subscribe({
      next: (response) => {
        debugger;
        if (response.status) {
          alert("Password Resetted Successfully!");
          this.router.navigate(['/admin-login']);
        } else {
          this.errorMessage = response.statusMessage;
        }
      },
      error: (err) => {
        this.errorMessage = err.statusMessage;
      },
    });
  }

  goBack():void{
    this.router.navigate(['/admin-login']);
  }
}
