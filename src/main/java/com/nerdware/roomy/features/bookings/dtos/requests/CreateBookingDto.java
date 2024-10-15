package com.nerdware.roomy.features.bookings.dtos.requests;

import java.time.LocalDate;

public record CreateBookingDto(Integer officeId, Integer userId, LocalDate date) {}
