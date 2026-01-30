export interface SubmissionDetails{
    candidateId:string,
    candidateFullName:string,
    interviewerId:string,
    codeType:string,
    code:string,
    time:string,
    actionPerformed:string,
    status:string,
    output:string,
    round:string
}

export interface AllSubmissions{
    statusMessage:string,
    response:SubmissionDetails[]
}