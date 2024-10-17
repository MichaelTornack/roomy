package com.nerdware.roomy.features.bookings.dtos.responses;

import com.nerdware.roomy.domain.entities.Booking;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public record BookingDto(Integer id, Integer userId, Integer officeId, java.time.LocalDate date)
{

    public static BookingDto toDto(Booking booking)
    {
        return new BookingDto(booking.getId(), booking.getUserId(), booking.getOfficeId(), booking.getDate());
    }

    public static List<BookingDto> toDto(Iterable<Booking> bookings)
    {
        return StreamSupport.stream(bookings.spliterator(), false)
            .map(entity -> toDto(entity))
            .collect(Collectors.toList());
    }
}
