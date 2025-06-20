import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterModule, Router } from '@angular/router'; 
import { AdminService } from '../../services/admin.service';

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

  constructor(private http: HttpClient, private router: Router,private adminService:AdminService) { }

  navigateTo(path: string): void {
    if(sessionStorage.getItem("isAdmin")==="false" && path==='/new-admin'){
      alert("You can't access this section!");
      return;
    }
    this.router.navigate([path]);
  }

  logOut():void{
    this.adminService.logoutAdmin();
    this.router.navigate(['/admin-login']);
  }
}