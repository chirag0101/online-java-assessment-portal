package com.iris.OnlineCompilerBackend.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "OC_TBL_CANDIDATES")
public class Candidate {
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @JoinColumn(name = "CANDIDATE_MASTER_ID_FK")
    @ManyToOne(fetch = FetchType.LAZY)
    private CandidateMaster candidateIdFk;

    @Column(name = "CANDIDATE_ID_CODE")
    private String candidateIdCode;

    @Column(name = "YEAR_OF_EXPERIENCE")
    private Double yearsOfExperience;

    @Column(name = "TECHNOLOGY")
    private String technology;

    @Column(name = "INTERVIEWER_ID")
    private String interviewerId;

    @Column(name = "URL_CREATED_TIME")
    private Date urlCreatedTime;

    @Column(name = "URL_EXPIRY_TIME")
    private Date urlExpiryTime;

    @Column(name = "URL")
    private String url;

    @Column(name = "URL_IS_ACTIVE")
    private Boolean urlIsActive;

    @Column(name = "URL_HASH_CODE")
    private String urlHashCode;

    @Column(name = "ASSESSMENT_IS_STARTED")
    private Boolean assessmentIsStarted;

    @Column(name = "ASSESSMENT_START_TIME")
    private Date assessmentStartTime;

    @Column(name = "ASSESSMENT_END_TIME")
    private Date assessmentEndTime;

    @ManyToOne
    @JoinColumn(name = "ASSESSMENT_ASSIGNED_BY_Fk")
    private Admin assessmentAssignedBy;

    @Column(name = "INTERVIEW_ROUND")
    private String interviewRound;

    @Column(name = "IS_REVIEWED")
    private Boolean isReviewed;

    @Column(name = "SCORE")
    private Double score;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ASSESSMENT_IS_ENDED")
    private Boolean assessmentIsEnded;

    @Column(name = "ASSESSMENT_DATE_TIME")
    private Date assessmentDateTime;

    public Candidate() {
    }

    public Candidate(Long userId, CandidateMaster candidateIdFk, String candidateIdCode, Double yearsOfExperience, String technology, String interviewerId, Date urlCreatedTime, Date urlExpiryTime, String url, Boolean urlIsActive, String urlHashCode, Boolean assessmentIsStarted, Date assessmentStartTime, Date assessmentEndTime, Admin assessmentAssignedBy, String interviewRound, Boolean isReviewed, Double score, String remarks, String status, Boolean assessmentIsEnded, Date assessmentDateTime) {
        this.userId = userId;
        this.candidateIdFk = candidateIdFk;
        this.candidateIdCode = candidateIdCode;
        this.yearsOfExperience = yearsOfExperience;
        this.technology = technology;
        this.interviewerId = interviewerId;
        this.urlCreatedTime = urlCreatedTime;
        this.urlExpiryTime = urlExpiryTime;
        this.url = url;
        this.urlIsActive = urlIsActive;
        this.urlHashCode = urlHashCode;
        this.assessmentIsStarted = assessmentIsStarted;
        this.assessmentStartTime = assessmentStartTime;
        this.assessmentEndTime = assessmentEndTime;
        this.assessmentAssignedBy = assessmentAssignedBy;
        this.interviewRound = interviewRound;
        this.isReviewed = isReviewed;
        this.score = score;
        this.remarks = remarks;
        this.status = status;
        this.assessmentIsEnded = assessmentIsEnded;
        this.assessmentDateTime = assessmentDateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CandidateMaster getCandidateIdFk() {
        return candidateIdFk;
    }

    public void setCandidateIdFk(CandidateMaster candidateIdFk) {
        this.candidateIdFk = candidateIdFk;
    }

    public String getCandidateIdCode() {
        return candidateIdCode;
    }

    public void setCandidateIdCode(String candidateIdCode) {
        this.candidateIdCode = candidateIdCode;
    }

    public Double getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Double yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getInterviewerId() {
        return interviewerId;
    }

    public void setInterviewerId(String interviewerId) {
        this.interviewerId = interviewerId;
    }

    public Date getUrlCreatedTime() {
        return urlCreatedTime;
    }

    public void setUrlCreatedTime(Date urlCreatedTime) {
        this.urlCreatedTime = urlCreatedTime;
    }

    public Date getUrlExpiryTime() {
        return urlExpiryTime;
    }

    public void setUrlExpiryTime(Date urlExpiryTime) {
        this.urlExpiryTime = urlExpiryTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getUrlIsActive() {
        return urlIsActive;
    }

    public void setUrlIsActive(Boolean urlIsActive) {
        this.urlIsActive = urlIsActive;
    }

    public String getUrlHashCode() {
        return urlHashCode;
    }

    public void setUrlHashCode(String urlHashCode) {
        this.urlHashCode = urlHashCode;
    }

    public Boolean getAssessmentIsStarted() {
        return assessmentIsStarted;
    }

    public void setAssessmentIsStarted(Boolean assessmentIsStarted) {
        this.assessmentIsStarted = assessmentIsStarted;
    }

    public Date getAssessmentStartTime() {
        return assessmentStartTime;
    }

    public void setAssessmentStartTime(Date assessmentStartTime) {
        this.assessmentStartTime = assessmentStartTime;
    }

    public Date getAssessmentEndTime() {
        return assessmentEndTime;
    }

    public void setAssessmentEndTime(Date assessmentEndTime) {
        this.assessmentEndTime = assessmentEndTime;
    }

    public Admin getAssessmentAssignedBy() {
        return assessmentAssignedBy;
    }

    public void setAssessmentAssignedBy(Admin assessmentAssignedBy) {
        this.assessmentAssignedBy = assessmentAssignedBy;
    }

    public String getInterviewRound() {
        return interviewRound;
    }

    public void setInterviewRound(String interviewRound) {
        this.interviewRound = interviewRound;
    }

    public Boolean getIsReviewed() {
        return isReviewed;
    }

    public void setIsReviewed(Boolean isReviewed) {
        this.isReviewed = isReviewed;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getAssessmentIsEnded() {
        return assessmentIsEnded;
    }

    public void setAssessmentIsEnded(Boolean assessmentIsEnded) {
        this.assessmentIsEnded = assessmentIsEnded;
    }

    public Date getAssessmentDateTime() {
        return assessmentDateTime;
    }

    public void setAssessmentDateTime(Date assessmentDateTime) {
        this.assessmentDateTime = assessmentDateTime;
    }
}