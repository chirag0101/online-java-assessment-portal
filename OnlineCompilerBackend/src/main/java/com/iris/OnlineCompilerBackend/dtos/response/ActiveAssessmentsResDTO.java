package com.iris.OnlineCompilerBackend.dtos.response;

import java.util.Date;

public class ActiveAssessmentsResDTO {
    private String candidateId;

    private String interviewerId;

    private String candidateFullName;

    private Double yearsOfExperience;

    private String technology;

    private Date urlExpiryTime;

    private Date assessmentStartTime;

    private Date assessmentEndTime;

    public ActiveAssessmentsResDTO() {
    }

    public ActiveAssessmentsResDTO(String candidateId, String interviewerId, String candidateFullName, Double yearsOfExperience, String technology, Date urlExpiryTime, Date assessmentStartTime, Date assessmentEndTime) {
        this.candidateId = candidateId;
        this.interviewerId = interviewerId;
        this.candidateFullName = candidateFullName;
        this.yearsOfExperience = yearsOfExperience;
        this.technology = technology;
        this.urlExpiryTime = urlExpiryTime;
        this.assessmentStartTime = assessmentStartTime;
        this.assessmentEndTime = assessmentEndTime;
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

    public String getCandidateFullName() {
        return candidateFullName;
    }

    public void setCandidateFullName(String candidateFullName) {
        this.candidateFullName = candidateFullName;
    }

    public Double getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Double yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public Date getUrlExpiryTime() {
        return urlExpiryTime;
    }

    public void setUrlExpiryTime(Date urlExpiryTime) {
        this.urlExpiryTime = urlExpiryTime;
    }

    public Date getAssessmentStartTime() {
        return assessmentStartTime;
    }

    public void setAssessmentStartTime(Date assessmentStartTime) {
        this.assessmentStartTime = assessmentStartTime;
    }

    public Date getAssessmentEndTime() {
        return assessmentEndTime;
    }

    public void setAssessmentEndTime(Date assessmentEndTime) {
        this.assessmentEndTime = assessmentEndTime;
    }
}
