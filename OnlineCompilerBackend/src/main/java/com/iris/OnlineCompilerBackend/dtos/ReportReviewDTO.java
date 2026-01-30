package com.iris.OnlineCompilerBackend.dtos;

import jakarta.validation.constraints.NotNull;

public class ReportReviewDTO {
    private String langType;

    @NotNull(message = "Score can't be empty!")
    private Double score;

    private String feedback;

    public ReportReviewDTO() {
    }

    public ReportReviewDTO(String langType, Double score, String feedback) {
        this.langType = langType;
        this.score = score;
        this.feedback = feedback;
    }

    public String getLangType() {
        return langType;
    }

    public void setLangType(String langType) {
        this.langType = langType;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}