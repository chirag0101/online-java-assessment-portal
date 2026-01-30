package com.iris.OnlineCompilerBackend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AssessmentExtensionReqDTO {
    @NotBlank(message = "CANDIDATE ID REQUIRED!")
    private String candidateId;

    @NotNull(message = "EXTENSION MINUTES REQUIRED!")
    private Long minutes;

    @NotBlank(message = "ROUND REQUIRED!")
    private String interviewRound;

    public AssessmentExtensionReqDTO() {
    }

    public AssessmentExtensionReqDTO(String candidateId, Long minutes, String interviewRound) {
        this.candidateId = candidateId;
        this.minutes = minutes;
        this.interviewRound = interviewRound;
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

    public String getInterviewRound() {
        return interviewRound;
    }

    public void setInterviewRound(String interviewRound) {
        this.interviewRound = interviewRound;
    }
}
