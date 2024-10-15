package com.nerdware.roomy.features.auth.dtos.responses;

public record TokenDto(String AccessToken, long ExpiresIn) {
}
