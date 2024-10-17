package com.nerdware.roomy.features.users.validators;

import com.nerdware.roomy.features.users.dtos.requests.CreateUserDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CreateUserValidator implements Validator
{

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
    }
}
