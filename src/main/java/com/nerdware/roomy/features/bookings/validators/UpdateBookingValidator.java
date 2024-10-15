package com.nerdware.roomy.features.bookings.validators;

import com.nerdware.roomy.features.bookings.dtos.requests.UpdateBookingDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class UpdateBookingValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateBookingDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var request = (UpdateBookingDto) target;

        if(request.date() != null && request.date().isBefore(LocalDate.now()))
            errors.rejectValue("officeId", "invalid date", "a correct date is required");
    }
}
