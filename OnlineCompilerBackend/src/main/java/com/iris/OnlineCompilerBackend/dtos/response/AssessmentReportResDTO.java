package com.iris.OnlineCompilerBackend.dtos.response;

import com.iris.OnlineCompilerBackend.dtos.AssessmentReportDTO;

import java.util.Date;
import java.util.List;

public class AssessmentReportResDTO {
    private String candidateId;
    private Boolean isReviewed;

    private List<AssessmentReportDTO> assessmentReports;
    private String finalFeedback;
    private Double finalAvgScore;

    private String interviewerId;
    private Date assessmentEndTime;
    private String round;
    private String status;

    public AssessmentReportResDTO() {
    }

    public AssessmentReportResDTO(String candidateId, Boolean isReviewed, List<AssessmentReportDTO> assessmentReports, String finalFeedback, Double finalAvgScore, String interviewerId, Date assessmentEndTime, String round, String status) {
        this.candidateId = candidateId;
        this.isReviewed = isReviewed;
        this.assessmentReports = assessmentReports;
        this.finalFeedback = finalFeedback;
        this.finalAvgScore = finalAvgScore;
        this.interviewerId = interviewerId;
        this.assessmentEndTime = assessmentEndTime;
        this.round = round;
        this.status = status;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public Boolean getIsReviewed() {
        return this.isReviewed;
    }

    public void setIsReviewed(Boolean isReviewed) {
        this.isReviewed = isReviewed;
    }

    public List<AssessmentReportDTO> getAssessmentReports() {
        return assessmentReports;
    }

    public void setAssessmentReports(List<AssessmentReportDTO> assessmentReports) {
        this.assessmentReports = assessmentReports;
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

    public Date getAssessmentEndTime() {
        return assessmentEndTime;
    }

    public void setAssessmentEndTime(Date assessmentEndTime) {
        this.assessmentEndTime = assessmentEndTime;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
