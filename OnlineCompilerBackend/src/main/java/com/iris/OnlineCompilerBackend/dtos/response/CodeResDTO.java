package com.iris.OnlineCompilerBackend.dtos.response;

import java.util.Date;

public class CodeResDTO {
    private String code;
    private String status;
    private String output;
    private String action;
    private Date time;
    private String round;

    public CodeResDTO() {
    }

    public CodeResDTO(String code, String status, String output, String action, Date time, String round) {
        this.code = code;
        this.status = status;
        this.output = output;
        this.action = action;
        this.time = time;
        this.round = round;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }
}
