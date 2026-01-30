export interface GetCandidateSubmissionsRes{
  code: string;
  status: string;
  output: string | null;
  action: string;
  time: string;
  round: string;
}

export interface CandidateSubmissionResponse {
  candidateName: string;
  codeType: string;
  candidateId: string;
  interviewerId: string;
  codeSubmissions: GetCandidateSubmissionsRes[];
}

export interface ApiResponse {
  status: boolean;
  statusMessage: string | null;
  response: CandidateSubmissionResponse;
}