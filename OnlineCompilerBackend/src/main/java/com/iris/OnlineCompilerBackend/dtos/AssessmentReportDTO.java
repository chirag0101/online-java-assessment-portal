package com.iris.OnlineCompilerBackend.dtos;

public class AssessmentReportDTO {
    private String langType;
    private Double score;
    private String comments;

    public AssessmentReportDTO() {
    }

    public AssessmentReportDTO(String langType, Double score, String comments) {
        this.langType = langType;
        this.score = score;
        this.comments = comments;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
