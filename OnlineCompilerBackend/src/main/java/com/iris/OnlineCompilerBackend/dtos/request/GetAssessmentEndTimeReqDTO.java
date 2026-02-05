package com.iris.OnlineCompilerBackend.dtos.request;

import jakarta.validation.constraints.NotBlank;

public class GetAssessmentEndTimeReqDTO {
    @NotBlank(message = "Candidate Id Required")
    private String candidateId;
    @NotBlank(message = "Interview Round Required")
    private String round;

    public GetAssessmentEndTimeReqDTO() {
    }

    public GetAssessmentEndTimeReqDTO(String candidateId, String round) {
        this.candidateId = candidateId;
        this.round = round;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }
}
