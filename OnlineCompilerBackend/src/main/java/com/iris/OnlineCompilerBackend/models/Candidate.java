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

    @Column(name = "CANDIDATE_ID")
    private String candidateId;

    @Column(name = "CANDIDATE_EMAIL_ID")
    private String candidateEmailId;

    @Column(name = "CANDIDATE_FULL_NAME")
    private String candidateFullName;

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

    public Candidate() {
    }

    public Candidate(Long userId, String candidateId, String candidateEmailId, String candidateFullName, Double yearsOfExperience, String technology, String interviewerId, Date urlCreatedTime, Date urlExpiryTime, String url, Boolean urlIsActive, String urlHashCode, Boolean assessmentIsStarted, Date assessmentStartTime, Date assessmentEndTime, Admin assessmentAssignedBy, String interviewRound) {
        this.userId = userId;
        this.candidateId = candidateId;
        this.candidateEmailId = candidateEmailId;
        this.candidateFullName = candidateFullName;
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
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateEmailId() {
        return candidateEmailId;
    }

    public void setCandidateEmailId(String candidateEmailId) {
        this.candidateEmailId = candidateEmailId;
    }

    public String getCandidateFullName() {
        return candidateFullName;
    }

    public void setCandidateFullName(String candidateFullName) {
        this.candidateFullName = candidateFullName;
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
}