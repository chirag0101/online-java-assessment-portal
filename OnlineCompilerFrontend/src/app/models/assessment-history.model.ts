export interface AssessmentHistory{
    candidateId:string,
    candidateName:string,
    interviewerId:string,
    technology:string,
    interviewRound:string,
    assessmentEndTime:string,
    status:string
}

export interface AllAssessmentHistories{
    statusMessage:string,
    response:AssessmentHistory[]
}