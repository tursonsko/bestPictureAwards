package com.backbase.bestPictureAwards.model.error;

import java.time.Instant;

public class ErrorModel {
    private int statusCode;
    private String message;
    private Instant time;

    public ErrorModel(int statusCode, String message, Instant time) {
        this.statusCode = statusCode;
        this.message = message;
        this.time = time;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}