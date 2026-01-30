package com.iris.OnlineCompilerBackend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class NewCandidateReqDTO {

    @NotBlank(message = "CANDIDATE ID REQUIRED!")
    private String candidateId;

    @NotBlank(message = "INTERVIEWER ID REQUIRED!")
    private String interviewerId;

    @NotBlank(message = "CANDIDATE EMAIL ID REQUIRED!")
    private String candidateEmailId;

    @NotBlank(message = "CANDIDATE FULLNAME REQUIRED!")
    private String candidateFullName;

    @NotBlank(message = "CANDIDATE TECHNOLOGY REQUIRED!")
    private String candidateTechnology;

    @NotNull(message = "CANDIDATE EXPERIENCE REQUIRED!")
    private Double candidateYearsOfExpInMonths;

    @NotBlank(message = "ADMIN ID REQUIRED!")
    private String adminId;

    @NotBlank(message = "INTERVIEW ROUND REQUIRED!")
    private String interviewRound;

    @NotNull(message = "INTERVIEW DATE/TIME REQUIRED")
    private Date interviewDateTime;

    public NewCandidateReqDTO() {
    }

    public NewCandidateReqDTO(String candidateId, String interviewerId, String candidateEmailId, String candidateFullName, String candidateTechnology, Double candidateYearsOfExpInMonths, String adminId, String interviewRound, Date interviewDateTime) {
        this.candidateId = candidateId;
        this.interviewerId = interviewerId;
        this.candidateEmailId = candidateEmailId;
        this.candidateFullName = candidateFullName;
        this.candidateTechnology = candidateTechnology;
        this.candidateYearsOfExpInMonths = candidateYearsOfExpInMonths;
        this.adminId = adminId;
        this.interviewRound = interviewRound;
        this.interviewDateTime = interviewDateTime;
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

    public String getCandidateEmailId() {
        return candidateEmailId;
    }

    public void setCandidateEmailId(String candidateEmailId) {
        this.candidateEmailId = candidateEmailId;
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

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
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
