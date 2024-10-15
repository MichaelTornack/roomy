package com.nerdware.roomy.domain.exceptions;

import org.springframework.validation.BindingResult;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(BindingResult validationErrors) {
        super(validationErrors.getAllErrors().getFirst().getDefaultMessage());
    }
}