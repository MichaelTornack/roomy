package com.nerdware.roomy.features.users.dtos.responses;

import com.nerdware.roomy.domain.entities.UserRole;

public record UserDto(int id, String email, UserRole role) {
}
