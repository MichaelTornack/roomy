package com.nerdware.roomy.features.bookings.dtos.requests;

import java.util.Optional;

public record GetBookingsDto(Optional<Integer> officeId, Optional<Integer> userId) {
}
