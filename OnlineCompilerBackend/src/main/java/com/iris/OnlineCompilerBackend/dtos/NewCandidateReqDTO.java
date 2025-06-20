package com.iris.OnlineCompilerBackend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
    private Integer candidateYearsOfExpInMonths;

    @NotBlank(message = "ADMIN ID REQUIRED!")
    private String adminId;

    @NotBlank(message = "INTERVIEW ROUND REQUIRED!")
    private String interviewRound;

    public NewCandidateReqDTO() {
    }

    public NewCandidateReqDTO(String candidateId, String interviewerId, String candidateEmailId, String candidateFullName, String candidateTechnology, Integer candidateYearsOfExpInMonths, String adminId, String interviewRound) {
        this.candidateId = candidateId;
        this.interviewerId = interviewerId;
        this.candidateEmailId = candidateEmailId;
        this.candidateFullName = candidateFullName;
        this.candidateTechnology = candidateTechnology;
        this.candidateYearsOfExpInMonths = candidateYearsOfExpInMonths;
        this.adminId = adminId;
        this.interviewRound = interviewRound;
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

    public Integer getCandidateYearsOfExpInMonths() {
        return candidateYearsOfExpInMonths;
    }

    public void setCandidateYearsOfExpInMonths(Integer candidateYearsOfExpInMonths) {
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
}
