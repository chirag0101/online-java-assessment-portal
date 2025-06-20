export interface SubmissionDetails{
    candidateId:string,
    candidateFullName:string,
    interviewerId:string,
    code:string,
    time:string,
    actionPerformed:string,
    status:string,
    output:string
}

export interface AllSubmissions{
    statusMessage:string,
    response:SubmissionDetails[]
}