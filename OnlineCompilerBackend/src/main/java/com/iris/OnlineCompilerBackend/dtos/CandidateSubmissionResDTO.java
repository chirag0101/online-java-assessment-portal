package com.iris.OnlineCompilerBackend.dtos;

import java.util.List;

public class CandidateSubmissionResDTO {
    private String candidateId;
    private List<CodeResDTO> codeSubmissions;

    public CandidateSubmissionResDTO() {
    }

    public CandidateSubmissionResDTO(String candidateId, List<CodeResDTO> codeSubmissions) {
        this.candidateId = candidateId;
        this.codeSubmissions = codeSubmissions;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public List<CodeResDTO> getCodeSubmissions() {
        return codeSubmissions;
    }

    public void setCodeSubmissions(List<CodeResDTO> codeSubmissions) {
        this.codeSubmissions = codeSubmissions;
    }
}
