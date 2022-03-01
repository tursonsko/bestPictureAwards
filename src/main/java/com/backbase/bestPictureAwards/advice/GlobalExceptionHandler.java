package com.backbase.bestPictureAwards.advice;

import com.backbase.bestPictureAwards.exception.AcademyAwardNotFoundException;
import com.backbase.bestPictureAwards.exception.WrongRateException;
import com.backbase.bestPictureAwards.model.error.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;

/**
 * Global handler for exceptions with custom error model
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AcademyAwardNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleAcademyAwardNotFoundException(AcademyAwardNotFoundException ex) {
        return new ResponseEntity<>(new ErrorModel(HttpStatus.NOT_FOUND.value(),
                ex.getMessage(), Instant.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongRateException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleWrongRateException(WrongRateException ex) {
        return new ResponseEntity<>(new ErrorModel(HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(), Instant.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) {
        return new ResponseEntity<>(new ErrorModel(HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(), Instant.now()), HttpStatus.UNAUTHORIZED);
    }

}