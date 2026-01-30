package com.iris.OnlineCompilerBackend.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "OC_TBL_CODE_RESPONSES")
public class CodeSnippet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODE_ID")
    private Long codeId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "TIME")
    private Date time;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "OUTPUT")
    private String output;

    @Column(name = "ACTION")
    private String action;

    /*  pending **/
    @ManyToOne
    @JoinColumn(name = "USER_ID_FK")
    private Candidate userIdFk;

    @Column(name = "LANGUAGE_TYPE")
    private String languageType;

    public CodeSnippet() {
    }

    public CodeSnippet(Long codeId, String code, Date time, String status, String output, String action, Candidate userIdFk, String languageType) {
        this.codeId = codeId;
        this.code = code;
        this.time = time;
        this.status = status;
        this.output = output;
        this.action = action;
        this.userIdFk = userIdFk;
        this.languageType = languageType;
    }

    public Long getCodeId() {
        return codeId;
    }

    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Candidate getUserIdFk() {
        return userIdFk;
    }

    public void setUserIdFk(Candidate userIdFk) {
        this.userIdFk = userIdFk;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }
}