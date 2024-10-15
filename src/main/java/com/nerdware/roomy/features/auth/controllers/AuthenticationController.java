package com.nerdware.roomy.features.auth.controllers;

import com.nerdware.roomy.domain.entities.User;
import com.nerdware.roomy.features.auth.dtos.requests.LoginDto;
import com.nerdware.roomy.features.auth.dtos.responses.TokenDto;
import com.nerdware.roomy.features.auth.services.AuthenticationService;
import com.nerdware.roomy.features.auth.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API for authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody LoginDto request) {

        User authenticatedUser = authenticationService.authenticate(request);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        var loginResponse = new TokenDto(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);

    }
}
