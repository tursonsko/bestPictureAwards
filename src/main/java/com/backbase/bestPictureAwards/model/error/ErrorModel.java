package com.backbase.bestPictureAwards.model.error;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Error Model to return fromatted Exceptions
 */
@Getter
@Setter
public class ErrorModel {
    private int statusCode;
    private String message;
    private Instant time;

    public ErrorModel(int statusCode, String message, Instant time) {
        this.statusCode = statusCode;
        this.message = message;
        this.time = time;
    }
}