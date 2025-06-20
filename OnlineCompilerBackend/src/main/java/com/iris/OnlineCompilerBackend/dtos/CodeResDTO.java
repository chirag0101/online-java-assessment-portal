package com.iris.OnlineCompilerBackend.dtos;

public class CodeResDTO {
    private String code;
    private String status;
    private String output;
    private String action;

    public CodeResDTO() {
    }

    public CodeResDTO(String code, String status, String output, String action) {
        this.code = code;
        this.status = status;
        this.output = output;
        this.action = action;
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
}
