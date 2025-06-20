import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { EMPTY, Observable } from 'rxjs';
import { CodeSnippetReqDTO } from '../../app/models/code-snippet-req.model';
import { CodeSnippetResDTO } from '../../app/models/code-snippet-res.model';
import { CompilerAction } from '../../app/models/constants/compiler-actions.constants';
import { AssessmentVerificationResponse } from '../../app/models/assessment-verification-res.model';
import { CandidateDetails } from '../../app/models/candidate-details.model';
import { AllSubmissions, SubmissionDetails } from '../../app/models/submission-details.model';

export interface ActiveAssessment {
  candidateNameWithId: string,
  interviwerNameWithId: string,
  yearsOfExperience: number,
  technology: string,
  round:string,
  urlExpiryTime: string,
  assessmentStartTime: string,
  assessmentEndTime: string,
  url:string
}

export interface ExtendAssessment{
  candidateId:string;
  minutes:number;
}

export interface ActiveAssessmentsResponse {
  statusMessage: string;
  response: ActiveAssessment[];
}

export interface NewAssessmentResponse {
  statusMessage: string;
  response: string;
}

export interface AllCandidates{
  statusMessage:string;
  response: CandidateDetails[];
}

export interface CandidateDetialsReq{
  candidateId:string,
  interviewerId:string,
  candidateFullName:string,
  candidateEmailId:string,
  candidateTechnology:string,
  candidateYearsOfExpInMonths:number|null,
  adminId:string,
  interviewRound:string
}

export interface CandidateDetialsRes{
  status:string,
  statusMessage:string,
  response:CandidateDetialsReq
}

export interface UrlRes{
  status:string,
  statusMessage:string,
  response:string
}

export interface CodeDetail{
  action:string,
  status:string,
  output:string
}

export interface CodeDetailRes{
  status:string,
  statusMessge:string,
  response:CodeDetail
}

export interface JdkVersionRes{
  status:string,
  statusMessge:string,
  response:string
}

@Injectable({
  providedIn: 'root'
})
export class AssessmentService {
  private compilerActionUrl = 'http://localhost:8080/CompilerService';
  private assessmentUrl = 'http://localhost:8080/AssessmentService';

  constructor(private http: HttpClient) {}

  getJdkVersion():Observable<JdkVersionRes>{
    return this.http.get<JdkVersionRes>(`${this.compilerActionUrl}/jdk-version`);
  }

  verifyAssessmentCode(assessmentId: string): Observable<AssessmentVerificationResponse> {
    return this.http.get<AssessmentVerificationResponse>(`${this.assessmentUrl}/assessment`, {
      params: { assessmentCode: assessmentId }
    });
  }

  compileCode(codeSnippetReqDTO: CodeSnippetReqDTO): Observable<CodeSnippetResDTO> {
    if (codeSnippetReqDTO.code == "") {
      alert("Code can't be empty!");
      console.log("Code Empty!");
      return EMPTY;
    }
    return this.http.post<CodeSnippetResDTO>(`${this.compilerActionUrl}/candidate-action/${CompilerAction.Compile}`, codeSnippetReqDTO);
  }

  runCode(codeSnippetReqDTO: CodeSnippetReqDTO): Observable<CodeSnippetResDTO> {
    if (codeSnippetReqDTO.code == "") {
      alert("Code can't be empty!");
      console.log("Code Empty!");
      return EMPTY;
    }
    return this.http.post<CodeSnippetResDTO>(`${this.compilerActionUrl}/candidate-action/${CompilerAction.Run}`, codeSnippetReqDTO);
  }

  createAssessment(candidateDetialsReq:CandidateDetialsReq):Observable<NewAssessmentResponse>{
    return this.http.post<NewAssessmentResponse>(`${this.assessmentUrl}/new-assessment`,candidateDetialsReq);
  }

  endAssessment(candidateId: string): Observable<any> {
    return this.http.post(`${this.assessmentUrl}/end-assessment/${candidateId}`, null);
  }

  getActiveAssessments(): Observable<ActiveAssessmentsResponse> {
    return this.http.get<ActiveAssessmentsResponse>(`${this.assessmentUrl}/active-assessments`);
  }

  getActiveAssessmentsForCurrentAdmin(adminId:string):Observable<ActiveAssessmentsResponse>{
    const interviewerId=sessionStorage.getItem("adminId")??'';
    let params = new HttpParams()
                .set('interviewer-id',interviewerId);
    return this.http.get<ActiveAssessmentsResponse>(`${this.assessmentUrl}/interviewer-active-assessments`,{params});
  }

  extendAssessment(extendAssessment:ExtendAssessment):Observable<any>{
    return this.http.post(`${this.assessmentUrl}/extend-assessment`,extendAssessment);
  }

  getAllCandidates():Observable<AllCandidates>{
    return this.http.get<AllCandidates>(`${this.assessmentUrl}/all-candidates`);
  }

  getAllSubmissions(): Observable<AllSubmissions> {
    return this.http.get<AllSubmissions>(`${this.assessmentUrl}/all-submissions`);
  }

  getSubmissionsByAdminId(adminId:string):Observable<AllSubmissions>{
    return this.http.get<AllSubmissions>(`${this.assessmentUrl}/view-submissions-for-interviewer/${adminId}`);
  }

  getCodeByID(codeId:string):Observable<CodeDetailRes>{
    return this.http.get<CodeDetailRes>(`${this.assessmentUrl}/get-code/${codeId}` );
  }

  getCandidateDetByEmail(candidateEmail:string):Observable<CandidateDetialsRes>{
    return this.http.get<CandidateDetialsRes>(`${this.assessmentUrl}/userdet-by-email/${candidateEmail}`);  
  }

  getUrlByCandidateId(candidateId:string):Observable<UrlRes>{
    return this.http.get<UrlRes>(`${this.assessmentUrl}/candidate-url/${candidateId}`);
  }
}