package com.nerdware.roomy.features.users.validators;

import com.nerdware.roomy.features.users.dtos.requests.UpdateUserDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UpdateUserValidator implements Validator
{
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";


    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateUserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var request = (UpdateUserDto) target;

        if(hasEmail(request) && !request.email().matches(EMAIL_PATTERN))
            errors.rejectValue("email", "email invalid", "Email is invalid");
    }

    public boolean hasEmail(UpdateUserDto request) {
        return request.email() != null && !request.email().isEmpty();
    }
}
