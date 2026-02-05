import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { EMPTY, Observable } from 'rxjs';
import { CodeSnippetReqDTO } from '../models/request/code-snippet-req.model';
import { CodeSnippetResDTO } from '../models/response/code-snippet-res.model';
import { CompilerAction } from '../../app/models/constants/compiler-actions.constants';
import { AssessmentVerificationResponse } from '../../app/models/response/assessment-verification-res.model';
import { CandidateDetails } from '../../app/models/candidate-details.model';
import { AllSubmissions } from '../../app/models/submission-details.model';
import { environment } from '../../environments/environment';
import {
  AllAssessmentHistories,
  AssessmentHistory,
} from '../models/assessment-history.model';
import { ViewReportRes } from '../models/response/view-report-res.model';
import { ReviewAssessmentComponent } from '../components/review-assessment/review-assessment.component';
import { SubmitReportReq } from '../models/request/submit-report-req.model';
import { GetCandidateSubmissionsReq } from '../models/request/candidate-submissions-req-model';
import { ApiResponse } from '../models/response/candidate-submissions-res.model';
import { ExpireAssessmentReq } from '../models/request/expire-assessment-req-model';
import { ViewReportReq } from '../models/request/view-report-req.model';
import { AssessmentEndTimeReq } from '../models/request/assessment-end-time-req.model';

export interface ActiveAssessment {
  candidateNameWithId: string;
  interviewerNameWithId: string;
  yearsOfExperience: number;
  technology: string;
  round: string;
  urlExpiryTime: string;
  assessmentScheduledTime:string,
  assessmentStartTime: string;
  assessmentEndTime: string;
  assessmentIsStarted:boolean;
  url: string;
}

export interface ExtendAssessment {
  candidateId: string;
  minutes: number;
  interviewRound:string;
}

export interface ActiveAssessmentsResponse {
  statusMessage: string;
  response: ActiveAssessment[];
}

export interface NewAssessmentResponse {
  status: boolean;
  statusMessage: string;
  response: string;
}

export interface AllCandidates {
  statusMessage: string;
  response: CandidateDetails[];
}

export interface CandidateDetialsReq {
  candidateId: string;
  interviewerId: string;
  candidateFullName: string;
  candidateEmailId: string;
  candidateTechnology: string;
  candidateYearsOfExpInMonths: number | null;
  adminId: string;
  interviewRound: string;
  interviewDateTime: string;
}

export interface CandidateDetialsRes {
  status: string;
  statusMessage: string;
  response: CandidateDetialsReq;
}

export interface UrlRes {
  status: string;
  statusMessage: string;
  response: string;
}

export interface CodeDetail {
  action: string;
  status: string;
  output: string;
}

export interface CodeDetailRes {
  status: string;
  statusMessge: string;
  response: CodeDetail;
}

export interface JdkVersionRes {
  status: string;
  statusMessge: string;
  response: string;
}

export interface AssessmentEndTimeRes {
  status: string;
  statusMessge: string;
  response: string;
}

@Injectable({
  providedIn: 'root',
})
export class AssessmentService {
  private compilerActionUrl = environment.compilerServiceUrl;
  private assessmentUrl = environment.assessmentServiceUrl;

  constructor(private http: HttpClient) {}

  getJdkVersion(): Observable<JdkVersionRes> {
    return this.http.get<JdkVersionRes>(
      `${this.compilerActionUrl}/jdkVersion`
    );
  }

  verifyAssessmentCode(
    assessmentId: string
  ): Observable<AssessmentVerificationResponse> {
    return this.http.get<AssessmentVerificationResponse>(
      `${this.assessmentUrl}/assessment`,
      {
        params: { assessmentCode: assessmentId },
      }
    );
  }

  compileCode(
    codeSnippetReqDTO: CodeSnippetReqDTO
  ): Observable<CodeSnippetResDTO> {
    if (codeSnippetReqDTO.code == '') {
      alert("Code can't be empty!");
      return EMPTY;
    }
    return this.http.post<CodeSnippetResDTO>(
      `${this.compilerActionUrl}/candidateAction/${CompilerAction.Compile}`,
      codeSnippetReqDTO
    );
  }

  saveCode(codeSnippetReqDTO: CodeSnippetReqDTO): Observable<CodeSnippetResDTO> {
    if (codeSnippetReqDTO.code == '') {
      alert("Code can't be empty!");
      return EMPTY;
    }
    return this.http.post<CodeSnippetResDTO>(
      `${this.compilerActionUrl}/submitData`,
      codeSnippetReqDTO
    );
  }

  runCode(codeSnippetReqDTO: CodeSnippetReqDTO): Observable<CodeSnippetResDTO> {
    if (codeSnippetReqDTO.code == '') {
      alert("Code can't be empty!");
      return EMPTY;
    }
    return this.http.post<CodeSnippetResDTO>(
      `${this.compilerActionUrl}/candidateAction/${CompilerAction.Run}`,
      codeSnippetReqDTO
    );
  }

  createAssessment(
    candidateDetialsReq: CandidateDetialsReq
  ): Observable<NewAssessmentResponse> {
    const header = {
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    };

    return this.http.post<NewAssessmentResponse>(
      `${this.assessmentUrl}/newAssessment`,
      candidateDetialsReq,
      { headers: header }
    );
  }

  endAssessment(req:ExpireAssessmentReq): Observable<any> {
    return this.http.post(
      `${this.assessmentUrl}/endAssessment`,
       req
    );
  }

  endAssessmentByAdmin(req:ExpireAssessmentReq): Observable<any> {
    const header = {
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    };

    return this.http.post(
      `${this.assessmentUrl}/endAssessmentByAdmin`,
       req,
      { headers: header }
    );
  }

  getActiveAssessments(): Observable<ActiveAssessmentsResponse> {
    const header = {
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    };

    return this.http.get<ActiveAssessmentsResponse>(
      `${this.assessmentUrl}/activeAssessments`,
      { headers: header }
    );
  }

  getActiveAssessmentsForCurrentAdmin(
    adminId: string
  ): Observable<ActiveAssessmentsResponse> {
    const header = {
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    };

    return this.http.get<ActiveAssessmentsResponse>(
      `${this.assessmentUrl}/interviewerActiveAssessments`,
      { headers: header }
    );
  }

  extendAssessment(extendAssessment: ExtendAssessment): Observable<any> {
    const header = {
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    };

    return this.http.post(
      `${this.assessmentUrl}/extendAssessment`,
      extendAssessment,
      { headers: header }
    );
  }

  getAllCandidates(): Observable<AllCandidates> {
    const header = {
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    };
    return this.http.get<AllCandidates>(
      `${this.assessmentUrl}/allCandidates`,
      { headers: header }
    );
  }

  getAllSubmissions(): Observable<AllSubmissions> {
    const header = {
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    };
    return this.http.get<AllSubmissions>(
      `${this.assessmentUrl}/allSubmissions`,
      { headers: header }
    );
  }

  getSubmissionsByAdminId(adminId: string): Observable<AllSubmissions> {
    const header = {
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    };

    return this.http.get<AllSubmissions>(
      `${this.assessmentUrl}/viewSubmissionsForInterviewer/${adminId}`,
      { headers: header }
    );
  }

  getCodeByID(codeId: string): Observable<CodeDetailRes> {
    return this.http.get<CodeDetailRes>(
      `${this.assessmentUrl}/getCode/${codeId}`
    );
  }

  getCandidateDetByEmail(
    candidateEmail: string
  ): Observable<CandidateDetialsRes> {
    const headers = new HttpHeaders({
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    });

    return this.http.get<CandidateDetialsRes>(
      `${this.assessmentUrl}/userDetByEmail/${candidateEmail}`,
      { headers }
    );
  }

  getUrlByCandidateId(candidateId: string): Observable<UrlRes> {
    const headers = new HttpHeaders({
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    });

    return this.http.get<UrlRes>(
      `${this.assessmentUrl}/candidateUrl/${candidateId}`,
      { headers: headers }
    );
  }

  getAssessmentEndTimeByCandId(
    req: AssessmentEndTimeReq,
  ): Observable<AssessmentEndTimeRes> {
    return this.http.post<AssessmentEndTimeRes>(
      `${this.assessmentUrl}/assessmentEndTimeByCandidateId`,
      req
    );
  }

  getCandidatesStatusByAdminId(
    adminId: string
  ): Observable<AllAssessmentHistories> {
    const header = {
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    };
    return this.http.get<AllAssessmentHistories>(
      `${this.assessmentUrl}/candidatesStatus/${adminId}`,
      { headers: header }
    );
  }

  getCandidateReport(viewReportReq: ViewReportReq): Observable<ViewReportRes> {
    const header = {
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    };
    return this.http.post<ViewReportRes>(
      `${this.assessmentUrl}/viewReport`,
      viewReportReq,
      { headers: header }
    );
  }

  submitCandidateReview(submitReportReq: SubmitReportReq): Observable<any> {
    const header = {
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    };
    return this.http.post<any>(
      `${this.assessmentUrl}/submitReview`,
      submitReportReq,
      { headers: header }
    );
  }

  getCandidateSubmissionsByCandidateId(
    req: GetCandidateSubmissionsReq
  ): Observable<ApiResponse> {
    const header = {
      accessToken: sessionStorage.getItem('accessToken') || '',
      adminId: sessionStorage.getItem('adminId') || '',
    };
    return this.http.post<ApiResponse>(
      `${this.assessmentUrl}/viewSubmissionsByCandidateId`,
      req,
      { headers: header }
    );
  }
}
