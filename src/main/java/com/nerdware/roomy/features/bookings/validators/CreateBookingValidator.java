package com.nerdware.roomy.features.bookings.validators;

import com.nerdware.roomy.features.bookings.dtos.requests.CreateBookingDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class CreateBookingValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CreateBookingDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var request = (CreateBookingDto) target;

        if(request.userId() == null)
            errors.rejectValue("userId", "invalid userId", "userId is required");

        if(request.officeId() == null)
            errors.rejectValue("officeId", "invalid officeId", "officeId is required");

        if(request.date() == null || request.date().isBefore(LocalDate.now()))
            errors.rejectValue("officeId", "invalid date", "a correct date is required");
    }
}
