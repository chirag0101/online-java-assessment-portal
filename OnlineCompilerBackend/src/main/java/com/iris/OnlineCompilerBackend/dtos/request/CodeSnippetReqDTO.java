package com.iris.OnlineCompilerBackend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CodeSnippetReqDTO {

    @NotBlank(message = "Candidate Id Required!")
    private String candidateId;

    @NotBlank(message = "Code can't be Empty!")
    private String code;

    @NotNull(message = "Code Type can't be Empty!")
    private Integer codeType;

    @NotNull(message = "Url can't be Empty!")
    private String url;

    public CodeSnippetReqDTO() {
    }

    public CodeSnippetReqDTO(String candidateId, String code, Integer codeType, String url) {
        this.candidateId = candidateId;
        this.code = code;
        this.codeType = codeType;
        this.url = url;
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

    public Integer getCodeType() {
        return codeType;
    }

    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}