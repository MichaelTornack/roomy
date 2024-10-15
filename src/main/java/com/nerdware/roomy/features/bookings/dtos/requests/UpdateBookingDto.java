package com.nerdware.roomy.features.bookings.dtos.requests;

import java.time.LocalDate;

public record UpdateBookingDto(Integer officeId, LocalDate date) {
}
