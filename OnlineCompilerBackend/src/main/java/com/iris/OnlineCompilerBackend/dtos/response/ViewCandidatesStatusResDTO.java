package com.iris.OnlineCompilerBackend.dtos.response;

import java.util.Date;

public class ViewCandidatesStatusResDTO {

    private String candidateId;

    private String candidateName;

    private String interviewerId;

    private String technology;

    private String interviewRound;

    private Date assessmentEndTime;

    private String status;

    public ViewCandidatesStatusResDTO() {
    }

    public ViewCandidatesStatusResDTO(String candidateId, String candidateName, String interviewerId, String technology, String interviewRound, Date assessmentEndTime, String status) {
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.interviewerId = interviewerId;
        this.technology = technology;
        this.interviewRound = interviewRound;
        this.assessmentEndTime = assessmentEndTime;
        this.status = status;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getInterviewerId() {
        return interviewerId;
    }

    public void setInterviewerId(String interviewerId) {
        this.interviewerId = interviewerId;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getInterviewRound() {
        return interviewRound;
    }

    public void setInterviewRound(String interviewRound) {
        this.interviewRound = interviewRound;
    }

    public Date getAssessmentEndTime() {
        return assessmentEndTime;
    }

    public void setAssessmentEndTime(Date assessmentEndTime) {
        this.assessmentEndTime = assessmentEndTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
