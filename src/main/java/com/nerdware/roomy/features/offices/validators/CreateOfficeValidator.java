package com.nerdware.roomy.features.offices.validators;

import com.nerdware.roomy.features.offices.dtos.requests.CreateOfficeDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CreateOfficeValidator  implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return CreateOfficeDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        var request = (CreateOfficeDto) target;

        if(request.name() == null || request.name().isEmpty())
            errors.rejectValue("name", "invalid name", "Name is required");

        if(request.capacity() == null || request.capacity() < 0)
            errors.rejectValue("capacity", "invalid capacity", "Capacity must be greater 0");

        if(request.location() == null || request.location().isEmpty())
            errors.rejectValue("location", "invalid location", "Location is required");
    }
}
