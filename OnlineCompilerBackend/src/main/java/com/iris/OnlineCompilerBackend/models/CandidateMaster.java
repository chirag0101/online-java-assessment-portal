package com.iris.OnlineCompilerBackend.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "OC_TBL_CANDIDATE_MASTER")
public class CandidateMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "CANDIDATE_ID")
    private String candidateId;

    @Column(name = "CANDIDATE_NAME")
    private String candidateName;

    @Column(name = "CANDIDATE_EMAIL")
    private String candidateEmail;

    @ManyToOne
    @JoinColumn(name = "CREATED_BY")
    private Admin createdBy;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    public CandidateMaster() {
    }

    public CandidateMaster(Long userId, String candidateId, String candidateName, String candidateEmail, Admin createdBy, Date createdOn) {
        this.userId = userId;
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
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

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public Admin getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Admin createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
