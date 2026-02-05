package com.iris.OnlineCompilerBackend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "OC_TBL_ASSESSMENT_REPORT")
public class AssessmentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCORE_ID")
    private Integer scoreId;

    @Column(name = "LANG_TYPE")
    private String langType;

    @Column(name = "SCORE")
    private Double score;

    @Column(name = "COMMENTS")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "CANDIDATE_USER_ID_FK")
    private Candidate candidateUserIdFk;

    public AssessmentReport() {
    }

    public AssessmentReport(String langType, Double score, String comments) {
        this.langType = langType;
        this.score = score;
        this.comments = comments;
    }

    public AssessmentReport(String langType, Double score, String comments, Candidate candidateUserIdFk) {
        this.langType = langType;
        this.score = score;
        this.comments = comments;
        this.candidateUserIdFk = candidateUserIdFk;
    }

    public Integer getScoreId() {
        return scoreId;
    }

    public void setScoreId(Integer scoreId) {
        this.scoreId = scoreId;
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

    public Candidate getCandidateUserIdFk() {
        return candidateUserIdFk;
    }

    public void setCandidateUserIdFk(Candidate candidateUserIdFk) {
        this.candidateUserIdFk = candidateUserIdFk;
    }
}
