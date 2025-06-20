package com.iris.OnlineCompilerBackend.dtos;

public class CodeSnippetResDTO {
    private final String status;
    private final String output;

    private CodeSnippetResDTO(Builder builder) {
        this.status = builder.status;
        this.output = builder.output;
    }

    public String getStatus() {
        return status;
    }

    public String getOutput() {
        return output;
    }

    public static class Builder {
        private String status;
        private String output;

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder statusMessage(String output) {
            this.output = output;
            return this;
        }

        public CodeSnippetResDTO build() {
            return new CodeSnippetResDTO(this);
        }
    }
}
