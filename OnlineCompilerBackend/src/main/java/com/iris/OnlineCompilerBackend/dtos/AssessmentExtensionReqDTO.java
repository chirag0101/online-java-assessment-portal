package com.iris.OnlineCompilerBackend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AssessmentExtensionReqDTO {
    @NotBlank(message = "CANDIDATE ID REQUIRED!")
    private String candidateId;

    @NotNull(message = "EXTENSION MINUTES REQUIRED!")
    private Long minutes;

    public AssessmentExtensionReqDTO() {
    }

    public AssessmentExtensionReqDTO(String candidateId, Long minutes) {
        this.candidateId = candidateId;
        this.minutes = minutes;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public Long getMinutes() {
        return minutes;
    }

    public void setMinutes(Long minutes) {
        this.minutes = minutes;
    }
}
