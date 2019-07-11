package com.iait.springbatchrest.payloads.responses;

public class ExecutionErrorResponse {

    private String stackTrace;

    public ExecutionErrorResponse() {}

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
