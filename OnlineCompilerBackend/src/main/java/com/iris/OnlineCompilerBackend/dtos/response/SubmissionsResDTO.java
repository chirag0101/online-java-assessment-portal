package com.iris.OnlineCompilerBackend.dtos.response;

import java.util.Date;

public class SubmissionsResDTO {
    private String candidateId;
    private String candidateFullName;
    private String interviewerId;
    private Date time;
    private String code;
    private String output;
    private String actionPerformed;
    private String status;

    public SubmissionsResDTO() {
    }

    public SubmissionsResDTO(String candidateId, String candidateFullName, String interviewerId, Date time, String code, String output, String actionPerformed, String status) {
        this.candidateId = candidateId;
        this.candidateFullName = candidateFullName;
        this.interviewerId = interviewerId;
        this.time = time;
        this.code = code;
        this.output = output;
        this.actionPerformed = actionPerformed;
        this.status = status;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateFullName() {
        return candidateFullName;
    }

    public void setCandidateFullName(String candidateFullName) {
        this.candidateFullName = candidateFullName;
    }

    public String getInterviewerId() {
        return interviewerId;
    }

    public void setInterviewerId(String interviewerId) {
        this.interviewerId = interviewerId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getActionPerformed() {
        return actionPerformed;
    }

    public void setActionPerformed(String actionPerformed) {
        this.actionPerformed = actionPerformed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
