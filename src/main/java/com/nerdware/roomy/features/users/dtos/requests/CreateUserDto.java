package com.nerdware.roomy.features.users.dtos.requests;

import com.nerdware.roomy.domain.entities.UserRole;

public record CreateUserDto(
        String name,
        String email,
        String password,
        UserRole role) {
}

