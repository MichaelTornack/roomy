package com.nerdware.roomy.features.users.dtos.responses;

import com.nerdware.roomy.domain.entities.User;
import com.nerdware.roomy.domain.entities.UserRole;

public record UserDto(int id, String email, UserRole role)
{

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getRole());
    }
}
