package com.iris.OnlineCompilerBackend.dtos.request;

public class ExpireAssessmentReqDTO {
    private String candidateId;
    private String assessmentUrl;
    private String interviewRound;

    public ExpireAssessmentReqDTO() {
    }

    public ExpireAssessmentReqDTO(String candidateId, String assessmentUrl, String interviewRound) {
        this.candidateId = candidateId;
        this.assessmentUrl = assessmentUrl;
        this.interviewRound = interviewRound;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getAssessmentUrl() {
        return assessmentUrl;
    }

    public void setAssessmentUrl(String assessmentUrl) {
        this.assessmentUrl = assessmentUrl;
    }

    public String getInterviewRound() {
        return interviewRound;
    }

    public void setInterviewRound(String interviewRound) {
        this.interviewRound = interviewRound;
    }
}
