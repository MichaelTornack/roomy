package com.nerdware.roomy.features.users.validators;

import com.nerdware.roomy.features.users.dtos.requests.CreateUserDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CreateUserValidator implements Validator
{
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";


    @Override
    public boolean supports(Class<?> clazz) {
        return CreateUserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        var request = (CreateUserDto) target;

        if(request.name() == null || request.name().isEmpty())  errors.rejectValue("name", "required", "Name is required");
        if(request.password() == null || request.password().isEmpty())  errors.rejectValue("password", "required", "Password is required");
        if(request.email() == null || request.email().isEmpty())  errors.rejectValue("email", "required", "Email is required");
        if(!request.email().matches(EMAIL_PATTERN)) errors.rejectValue("email", "email invalid", "Email is invalid");
    }
}
