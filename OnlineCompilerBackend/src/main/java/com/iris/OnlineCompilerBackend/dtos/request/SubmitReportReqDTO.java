package com.iris.OnlineCompilerBackend.dtos.request;

import com.iris.OnlineCompilerBackend.dtos.ReportReviewDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public class SubmitReportReqDTO {
    @NotEmpty(message = "Candidate Id Required")
    private String candidateId;

    private List<ReportReviewDTO> reviews;

    @NotNull(message = "Result Status Required")
    private boolean isPassed;

    @NotBlank(message = "Final Feedback is Required")
    private String finalFeedback;

    private Double finalAvgScore;

    private String interviewerId;
    private String round;
    private Date assessmentEndTime;

    public SubmitReportReqDTO() {
    }

    public SubmitReportReqDTO(String candidateId, List<ReportReviewDTO> reviews, boolean isPassed, String finalFeedback, Double finalAvgScore, String interviewerId, String round, Date assessmentEndTime) {
        this.candidateId = candidateId;
        this.reviews = reviews;
        this.isPassed = isPassed;
        this.finalFeedback = finalFeedback;
        this.finalAvgScore = finalAvgScore;
        this.interviewerId = interviewerId;
        this.round = round;
        this.assessmentEndTime = assessmentEndTime;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public List<ReportReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReportReviewDTO> reviews) {
        this.reviews = reviews;
    }

    public boolean getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(boolean isPassed) {
        this.isPassed = isPassed;
    }

    public String getFinalFeedback() {
        return finalFeedback;
    }

    public void setFinalFeedback(String finalFeedback) {
        this.finalFeedback = finalFeedback;
    }

    public Double getFinalAvgScore() {
        return finalAvgScore;
    }

    public void setFinalAvgScore(Double finalAvgScore) {
        this.finalAvgScore = finalAvgScore;
    }

    public String getInterviewerId() {
        return interviewerId;
    }

    public void setInterviewerId(String interviewerId) {
        this.interviewerId = interviewerId;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public Date getAssessmentEndTime() {
        return assessmentEndTime;
    }

    public void setAssessmentEndTime(Date assessmentEndTime) {
        this.assessmentEndTime = assessmentEndTime;
    }

}
