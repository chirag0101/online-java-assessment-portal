import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, UrlTree } from '@angular/router';
import { AdminService } from '../services/admin.service'; // <-- Import your service

@Injectable({
  providedIn: 'root'
})
export class AdminAuthGuard implements CanActivate {

  constructor(private adminService: AdminService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean | UrlTree {

    if (this.adminService.isAuthenticated()) {
      return true;
    } else {
      this.router.navigate(['/adminLogin']);
      return false;
    }
  }
}