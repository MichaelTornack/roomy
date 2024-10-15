package com.nerdware.roomy.features.bookings.dtos.responses;

public record BookingDto(Integer id, Integer userId, Integer officeId, java.time.LocalDate date) {
}
