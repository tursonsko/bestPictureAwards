package com.backbase.bestPictureAwards.advice;

import com.backbase.bestPictureAwards.exception.AcademyAwardNotFoundException;
import com.backbase.bestPictureAwards.exception.WrongRateException;
import com.backbase.bestPictureAwards.model.error.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AcademyAwardNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleAcademyAwardNotFoundException(AcademyAwardNotFoundException ex) {
        return new ResponseEntity<>(new ErrorModel(HttpStatus.NOT_FOUND.value(),
                ex.getMessage(), Instant.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongRateException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleWrongRateException(WrongRateException ex) {
        return new ResponseEntity<>(new ErrorModel(HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(), Instant.now()), HttpStatus.BAD_REQUEST);
    }

}
