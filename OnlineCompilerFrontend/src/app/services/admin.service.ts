import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { AdminLoginCreds } from '../../app/models/admin-login-creds.model';
import { AdminLoginResponse } from '../../app/models/admin-login-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NewAdminResponse } from '../../app/components/new-admin/new-admin.component';
import { Observable, BehaviorSubject, of, catchError, tap } from 'rxjs';
import { AdminDetailsResponse } from '../models/all-admins-res.model';
import { NewAdminDetails } from '../models/admin-details.model';
import { isPlatformBrowser } from '@angular/common';
import { ResetAdminRes } from '../models/reset-admin-res.model';
import { ResetAdminReq } from '../models/reset-admin-req.model';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  private adminServiceUrl = environment.adminServiceUrl;

  public _isLoggedIn = new BehaviorSubject<boolean>(false);
  _isLoggedIn$ = this._isLoggedIn.asObservable();

  setIsLoggedIn(value: boolean) {
    this._isLoggedIn.next(value);
  }

  public isAuthenticatedUser: boolean = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    if (this.isBrowser()) {
      this.isAuthenticatedUser =
        sessionStorage.getItem('isAuthenticatedUser') === 'true';
      this._isLoggedIn.next(this.isAuthenticatedUser);
    } else {
      this.isAuthenticatedUser = false;
    }
  }

  private isBrowser(): boolean {
    return isPlatformBrowser(this.platformId);
  }

  authenticateAdmin(
    adminLoginCreds: AdminLoginCreds
  ): Observable<AdminLoginResponse> {
    return this.http
      .post<AdminLoginResponse>(
        `${this.adminServiceUrl}/authenticate-admin`,
        adminLoginCreds
      )
      .pipe(
        tap((response) => {
          if (response.status) {
            if (this.isBrowser()) {
              sessionStorage.setItem('isAuthenticatedUser', 'true');
              sessionStorage.setItem('adminId', response.response.adminId);
              sessionStorage.setItem(
                'isAdmin',
                response.response.isAdmin.toString()
              );
              sessionStorage.setItem(
                'adminFullName',
                response.response.adminFullName
              );
            }
            this.setIsLoggedIn(true);
            console.log('Admin authenticated successfully!');
          } else {
            if (this.isBrowser()) {
              sessionStorage.setItem('isAuthenticatedUser', 'false');
            }
            console.log('Admin authentication failed:', response.statusMessage);
          }
        }),
        catchError((error) => {
          this.setIsLoggedIn(false);
          if (this.isBrowser()) {
            sessionStorage.setItem('isAuthenticatedUser', 'false');
          }
          console.error('Authentication API error:', error);
          throw error;
        })
      );
  }

  isAuthenticated(): boolean {
    if (this.isBrowser()) {
      const value = sessionStorage.getItem('isAuthenticatedUser');
      return value === 'true';
    }
    return false;
  }

  logoutAdmin(): void {
    this._isLoggedIn.next(false);
    if (this.isBrowser()) {
      sessionStorage.clear();
    }
    console.log('Admin logged out.');
  }

  newAdmin(newAdminDetails: NewAdminDetails): Observable<NewAdminResponse> {
    const header = {
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    };
    return this.http.post<NewAdminResponse>(
      `${this.adminServiceUrl}/new-admin`,
      newAdminDetails,
      { headers: header }
    );
  }

  resetPassword(resetAdminReq: ResetAdminReq): Observable<ResetAdminRes> {
    return this.http.post<ResetAdminRes>(
      `${this.adminServiceUrl}/reset-password`,
      resetAdminReq
    );
  }

  getAllAdmins(): Observable<AdminDetailsResponse> {
    const header = new HttpHeaders({
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    });

    return this.http.get<AdminDetailsResponse>(
      `${this.adminServiceUrl}/all-admins`,
      {
        headers: header,
      }
    );
  }

  onSessionTimeout(): void {
    sessionStorage.clear();
    alert('Session Timeout!');
    this.router.navigate(['/admin-login']);
  }
}
