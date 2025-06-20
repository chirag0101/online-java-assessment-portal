package com.iris.OnlineCompilerBackend.dtos;

import jakarta.validation.constraints.NotBlank;

public class CodeSnippetReqDTO {

    @NotBlank(message = "Candidate Id Required!")
    private String candidateId;

    @NotBlank(message = "Code can't be Empty!")
    private String code;

    public CodeSnippetReqDTO() {
    }

    public CodeSnippetReqDTO(String candidateId, String code) {
        this.candidateId = candidateId;
        this.code = code;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}