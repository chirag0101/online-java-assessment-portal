package com.iris.OnlineCompilerBackend.constants;

public enum ResponseStatus {
    FAILURE("FAILURE"),
    SUCCESS("SUCCESS");

    private final String status;

    ResponseStatus(String status) {
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

}
