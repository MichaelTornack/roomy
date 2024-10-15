package com.nerdware.roomy.features.offices.validators;

import com.nerdware.roomy.features.offices.dtos.requests.UpdateOfficeDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UpdateOfficeValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateOfficeDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        var request = (UpdateOfficeDto) target;

        if(request.capacity() != null && request.capacity() <= 0)
            errors.rejectValue("capacity", "invalid capacity", "Capacity must be greater 0");
    }
}
