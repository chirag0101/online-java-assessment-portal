package com.iris.OnlineCompilerBackend.dtos.response;

import java.util.Date;

public class ActiveAssessmentDetResDTO {

    private String candidateNameWithId;

    private String interviewerNameWithId;

    private Double yearsOfExperience;

    private String technology;

    private String round;

    private Date urlExpiryTime;

    private Date assessmentStartTime;

    private Date assessmentEndTime;

    private String url;

    private Date assessmentScheduledTime;

    private boolean assessmentIsStarted;

    public ActiveAssessmentDetResDTO() {
    }

    public ActiveAssessmentDetResDTO(String candidateNameWithId, String interviewerNameWithId, Double yearsOfExperience, String technology, String round, Date urlExpiryTime, Date assessmentStartTime, Date assessmentEndTime, String url, Date assessmentScheduledTime, boolean assessmentIsStarted) {
        this.candidateNameWithId = candidateNameWithId;
        this.interviewerNameWithId = interviewerNameWithId;
        this.yearsOfExperience = yearsOfExperience;
        this.technology = technology;
        this.round = round;
        this.urlExpiryTime = urlExpiryTime;
        this.assessmentStartTime = assessmentStartTime;
        this.assessmentEndTime = assessmentEndTime;
        this.url = url;
        this.assessmentScheduledTime = assessmentScheduledTime;
        this.assessmentIsStarted = assessmentIsStarted;
    }

    public String getCandidateNameWithId() {
        return candidateNameWithId;
    }

    public void setCandidateNameWithId(String candidateNameWithId) {
        this.candidateNameWithId = candidateNameWithId;
    }

    public String getInterviewerNameWithId() {
        return interviewerNameWithId;
    }

    public void setInterviewerNameWithId(String interviewerNameWithId) {
        this.interviewerNameWithId = interviewerNameWithId;
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

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getAssessmentScheduledTime() {
        return assessmentScheduledTime;
    }

    public void setAssessmentScheduledTime(Date assessmentScheduledTime) {
        this.assessmentScheduledTime = assessmentScheduledTime;
    }

    public boolean isAssessmentIsStarted() {
        return assessmentIsStarted;
    }

    public void setAssessmentIsStarted(boolean assessmentIsStarted) {
        this.assessmentIsStarted = assessmentIsStarted;
    }
}
