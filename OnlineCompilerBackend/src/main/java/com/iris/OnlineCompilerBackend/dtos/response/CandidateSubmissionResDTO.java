package com.iris.OnlineCompilerBackend.dtos.response;

import java.util.List;

public class CandidateSubmissionResDTO {
    private String candidateId;
    private String candidateName;
    private String codeType;
    private String interviewerId;
    private List<CodeResDTO> codeSubmissions;

    public CandidateSubmissionResDTO() {
    }

    public CandidateSubmissionResDTO(String candidateId, String candidateName, String codeType, String interviewerId, List<CodeResDTO> codeSubmissions) {
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.codeType = codeType;
        this.interviewerId = interviewerId;
        this.codeSubmissions = codeSubmissions;
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

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getInterviewerId() {
        return interviewerId;
    }

    public void setInterviewerId(String interviewerId) {
        this.interviewerId = interviewerId;
    }

    public List<CodeResDTO> getCodeSubmissions() {
        return codeSubmissions;
    }

    public void setCodeSubmissions(List<CodeResDTO> codeSubmissions) {
        this.codeSubmissions = codeSubmissions;
    }
}
