export interface SubmitReportReq{
    candidateId: string,
    reviews: Report[],
    isPassed: boolean,
    finalFeedback: string,
    finalAvgScore:number,
    interviewerId:string,
    round:string,
    assessmentEndTime:string
}

export interface Report{
    langType: string,
    score: number | null,
    feedback: string | null
}