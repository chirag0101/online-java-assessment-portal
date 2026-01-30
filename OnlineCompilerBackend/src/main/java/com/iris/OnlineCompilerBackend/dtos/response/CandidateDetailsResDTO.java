package com.iris.OnlineCompilerBackend.dtos.response;

import java.util.Date;

public class CandidateDetailsResDTO {

    private String candidateId;

    private String candidateFullName;

    private String candidateTechnology;

    private Double candidateYearsOfExpInMonths;

    private String candidateEmailId;

    private String interviewRound;

    private Date interviewDateTime;

    public CandidateDetailsResDTO() {
    }

    public CandidateDetailsResDTO(String candidateId, String candidateFullName, String candidateTechnology, Double candidateYearsOfExpInMonths, String candidateEmailId, String interviewRound) {
        this.candidateId = candidateId;
        this.candidateFullName = candidateFullName;
        this.candidateTechnology = candidateTechnology;
        this.candidateYearsOfExpInMonths = candidateYearsOfExpInMonths;
        this.candidateEmailId = candidateEmailId;
        this.interviewRound = interviewRound;
    }

    public CandidateDetailsResDTO(String candidateId, String candidateFullName, String candidateTechnology, Double candidateYearsOfExpInMonths, String candidateEmailId, String interviewRound, Date interviewDateTime) {
        this.candidateId = candidateId;
        this.candidateFullName = candidateFullName;
        this.candidateTechnology = candidateTechnology;
        this.candidateYearsOfExpInMonths = candidateYearsOfExpInMonths;
        this.candidateEmailId = candidateEmailId;
        this.interviewRound = interviewRound;
        this.interviewDateTime = interviewDateTime;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateFullName() {
        return candidateFullName;
    }

    public void setCandidateFullName(String candidateFullName) {
        this.candidateFullName = candidateFullName;
    }

    public String getCandidateTechnology() {
        return candidateTechnology;
    }

    public void setCandidateTechnology(String candidateTechnology) {
        this.candidateTechnology = candidateTechnology;
    }

    public Double getCandidateYearsOfExpInMonths() {
        return candidateYearsOfExpInMonths;
    }

    public void setCandidateYearsOfExpInMonths(Double candidateYearsOfExpInMonths) {
        this.candidateYearsOfExpInMonths = candidateYearsOfExpInMonths;
    }

    public String getCandidateEmailId() {
        return candidateEmailId;
    }

    public void setCandidateEmailId(String candidateEmailId) {
        this.candidateEmailId = candidateEmailId;
    }

    public String getInterviewRound() {
        return interviewRound;
    }

    public void setInterviewRound(String interviewRound) {
        this.interviewRound = interviewRound;
    }

    public Date getInterviewDateTime() {
        return interviewDateTime;
    }

    public void setInterviewDateTime(Date interviewDateTime) {
        this.interviewDateTime = interviewDateTime;
    }
}
