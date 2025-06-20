package com.iris.OnlineCompilerBackend.models;

public class ApiResponse {
    private final boolean status;
    private final String statusMessage;
    private final Object response;

    private ApiResponse(Builder builder) {
        this.status = builder.status;
        this.statusMessage = builder.statusMessage;
        this.response = builder.response;
    }

    public boolean getStatus() {
        return status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public Object getResponse() {
        return response;
    }

    public static class Builder {
        private boolean status;
        private String statusMessage;
        private Object response;

        public Builder status(boolean status) {
            this.status = status;
            return this;
        }

        public Builder statusMessage(String statusMessage) {
            this.statusMessage = statusMessage;
            return this;
        }

        public Builder response(Object response) {
            this.response = response;
            return this;
        }

        public ApiResponse build() {
            return new ApiResponse(this);
        }
    }
}