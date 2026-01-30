import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterModule, Router } from '@angular/router'; 
import { AdminService } from '../../services/admin.service';
import { NavbarText } from '../../models/constants/navbar-text.constants';

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    HttpClientModule
  ],
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent {

  version=NavbarText.Version;
  copyright=NavbarText.Copyright;
  portalTitle=NavbarText.PortalName;

  adminId=sessionStorage.getItem("adminFullName");
  adminLastLoggedIn=sessionStorage.getItem("lastLoggedIn");
  lastLoggedIn='';

  constructor(private http: HttpClient, private router: Router,private adminService:AdminService) {}

  navigateTo(path: string): void {
    if(sessionStorage.getItem("isAdmin")==="false" && path==='/newAdmin'){
      alert("You can't access this section!");
      return;
    }
    this.router.navigate([path]);
  }

  logOut():void{
    this.adminService.logoutAdmin();
    this.router.navigate(['/adminLogin']);
  }
}