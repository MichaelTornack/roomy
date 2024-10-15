package com.nerdware.roomy.features.bookings.dtos.responses;

import java.time.LocalDate;

public record BookingOverviewDto(
    LocalDate date,
    Integer capacity,
    Integer booked,
    boolean hasFreeSpaces) {}
