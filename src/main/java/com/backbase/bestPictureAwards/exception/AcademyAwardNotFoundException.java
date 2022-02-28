package com.backbase.bestPictureAwards.exception;

import java.util.function.Supplier;

public class AcademyAwardNotFoundException extends Exception {
    public AcademyAwardNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
