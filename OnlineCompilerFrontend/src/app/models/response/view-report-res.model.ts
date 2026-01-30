export interface AssessmentReport{
    langType: string,
    score: number | null,
    comments: string | null
}

export interface ViewReport{
        candidateId: string,
        isReviewed: boolean,
        assessmentReports: AssessmentReport[],
        finalFeedback:string,
        finalAvgScore:number,
        interviewerId:string,
        assessmentEndTime:string,
        round:string,
        status:string
}

export interface ViewReportRes{
    status:boolean,
    statusMessage:string,
    response:ViewReport
}