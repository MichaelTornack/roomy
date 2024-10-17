package com.nerdware.roomy.features.offices.controllers;

import com.nerdware.roomy.domain.entities.User;
import com.nerdware.roomy.domain.exceptions.BadRequestException;
import com.nerdware.roomy.features.offices.dtos.requests.CreateOfficeDto;
import com.nerdware.roomy.features.offices.dtos.requests.UpdateOfficeDto;
import com.nerdware.roomy.features.offices.dtos.responses.OfficeDto;
import com.nerdware.roomy.features.offices.services.OfficeService;
import com.nerdware.roomy.features.offices.validators.CreateOfficeValidator;
import com.nerdware.roomy.features.offices.validators.UpdateOfficeValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/offices")
@Tag(name = "Offices", description = "API for managing offices")
public class OfficeController
{
    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @Operation(summary = "Get office by ID")
    @GetMapping("/{id}")
    public ResponseEntity getOffice(@PathVariable Integer id)
        throws Exception
    {
        var office = officeService.getOfficeById(id);
        var officeDto = OfficeDto.toDto(office);
        return new ResponseEntity(officeDto, HttpStatus.OK);
    }

    @Operation(summary = "Get all offices")
    @GetMapping("")
    public ResponseEntity getOffices()
    {
        var offices = officeService.getAllOffices();

        var officesDto = StreamSupport.stream(offices.spliterator(), false).map((var o) -> OfficeDto.toDto(o))
            .collect(Collectors.toList());

        return new ResponseEntity(officesDto, HttpStatus.OK);
    }

    @Operation(summary = "Create new office")
    @PostMapping()
    public ResponseEntity createOffice(@Valid @RequestBody CreateOfficeDto request, Authentication authentication, BindingResult validatorErrors)
        throws Exception
    {
        var caller = (User) authentication.getPrincipal();

        var validator = new CreateOfficeValidator();
        validator.validate(request, validatorErrors);

        if(validatorErrors.hasErrors())
            throw new BadRequestException(validatorErrors);

        var office = officeService.createOffice(request, caller);

        var officeDto = OfficeDto.toDto(office);
        return new ResponseEntity(officeDto, HttpStatus.OK);
    }

    @Operation(summary = "Update office")
    @PatchMapping("/{id}")
    public ResponseEntity updateOffice(@PathVariable Integer id, @Valid @RequestBody UpdateOfficeDto request, Authentication authentication, BindingResult validatorErrors)
        throws Exception
    {
        var caller = (User) authentication.getPrincipal();

        var validator = new UpdateOfficeValidator();
        validator.validate(request, validatorErrors);

        if(validatorErrors.hasErrors())
            throw new BadRequestException(validatorErrors);

        var office = officeService.updateOffice(id, request, caller);

        var officeDto = OfficeDto.toDto(office);
        return new ResponseEntity(officeDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete office by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteOffice(@PathVariable Integer id, Authentication authentication)
        throws Exception
    {
        var caller = (User) authentication.getPrincipal();
        officeService.deleteOffice(id, caller);
        return new ResponseEntity(null, HttpStatus.OK);
    }
}
