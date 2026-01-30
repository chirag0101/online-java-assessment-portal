package com.iris.OnlineCompilerBackend.dtos.request;

import java.util.Date;

public class ViewCandidateSubmissionsReqDTO {

    private String candidateId;
    private String interviewerId;
    private String round;
    private Date assessmentEndTime;
    private String langType;

    public ViewCandidateSubmissionsReqDTO() {
    }

    public ViewCandidateSubmissionsReqDTO(String candidateId, String interviewerId, String round, Date assessmentEndTime, String langType) {
        this.candidateId = candidateId;
        this.interviewerId = interviewerId;
        this.round = round;
        this.assessmentEndTime = assessmentEndTime;
        this.langType = langType;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getInterviewerId() {
        return interviewerId;
    }

    public void setInterviewerId(String interviewerId) {
        this.interviewerId = interviewerId;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public Date getAssessmentEndTime() {
        return assessmentEndTime;
    }

    public void setAssessmentEndTime(Date assessmentEndTime) {
        this.assessmentEndTime = assessmentEndTime;
    }

    public String getLangType() {
        return langType;
    }

    public void setLangType(String langType) {
        this.langType = langType;
    }
}
