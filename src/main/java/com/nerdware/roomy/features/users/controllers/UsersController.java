package com.nerdware.roomy.features.users.controllers;

import com.nerdware.roomy.domain.entities.User;
import com.nerdware.roomy.domain.exceptions.BadRequestException;
import com.nerdware.roomy.domain.exceptions.ResourceNotFoundException;
import com.nerdware.roomy.features.users.dtos.requests.CreateUserDto;
import com.nerdware.roomy.features.users.dtos.requests.UpdateUserDto;
import com.nerdware.roomy.features.users.dtos.responses.UserDto;
import com.nerdware.roomy.features.users.services.UserService;
import com.nerdware.roomy.features.users.validators.CreateUserValidator;
import com.nerdware.roomy.features.users.validators.UpdateUserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API for managing users")
public class UsersController
{

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable Integer id) throws ResourceNotFoundException
    {
        var user = userService.getUserById(id);
        var userDto = new UserDto(user.getId(), user.getEmail(), user.getRole());
        return new ResponseEntity(userDto, HttpStatus.OK);
    }

    @Operation(summary = "Create new user")
    @PostMapping()
    public ResponseEntity createUser(@Valid @RequestBody CreateUserDto request, Authentication authentication, BindingResult validatorErrors)
        throws Exception
    {
        var caller = (User) authentication.getPrincipal();

        var validator = new CreateUserValidator();
        validator.validate(request, validatorErrors);

        if(validatorErrors.hasErrors())
            throw new BadRequestException(validatorErrors);

        var user = userService.createUser(request, caller);
        var userDto = new UserDto(user.getId(), user.getEmail(), user.getRole());
        return new ResponseEntity(userDto, HttpStatus.OK);
    }

    @Operation(summary = "Update user")
    @PatchMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable Integer id, @Valid @RequestBody UpdateUserDto request, Authentication authentication, BindingResult validatorErrors)
        throws Exception
    {
        var caller = (User) authentication.getPrincipal();

        var validator = new UpdateUserValidator();
        validator.validate(request, validatorErrors);

        if(validatorErrors.hasErrors())
            throw new BadRequestException(validatorErrors);

        var updated = userService.updateUser(id, request, caller);

        var userDto = new UserDto(updated.getId(), updated.getEmail(), updated.getRole());
        return new ResponseEntity(userDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id, Authentication authentication)
        throws Exception
    {
        var caller = (User) authentication.getPrincipal();
        userService.deleteUser(id, caller);
        return new ResponseEntity(null, HttpStatus.OK);
    }
}
