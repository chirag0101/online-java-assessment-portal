package com.iris.OnlineCompilerBackend.dtos;

public class CandidateDetailsResDTO {

    private String candidateId;

    private String candidateFullName;

    private String candidateTechnology;

    private Double candidateYearsOfExpInMonths;

    private String candidateEmailId;

    public CandidateDetailsResDTO() {
    }

    public CandidateDetailsResDTO(String candidateId, String candidateFullName, String candidateTechnology, Double candidateYearsOfExpInMonths, String candidateEmailId) {
        this.candidateId = candidateId;
        this.candidateFullName = candidateFullName;
        this.candidateTechnology = candidateTechnology;
        this.candidateYearsOfExpInMonths = candidateYearsOfExpInMonths;
        this.candidateEmailId = candidateEmailId;
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
}
