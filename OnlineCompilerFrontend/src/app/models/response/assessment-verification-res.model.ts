import { CandidateDetails } from '../candidate-details.model';

export interface AssessmentVerificationResponse{
    status:boolean,
    statusMessage:string,
    response:CandidateDetails
}