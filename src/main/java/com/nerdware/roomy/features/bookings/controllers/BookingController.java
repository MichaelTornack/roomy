package com.nerdware.roomy.features.bookings.controllers;

import com.nerdware.roomy.domain.entities.User;
import com.nerdware.roomy.domain.exceptions.BadRequestException;
import com.nerdware.roomy.features.bookings.dtos.requests.CreateBookingDto;
import com.nerdware.roomy.features.bookings.dtos.requests.GetBookingsDto;
import com.nerdware.roomy.features.bookings.dtos.requests.UpdateBookingDto;
import com.nerdware.roomy.features.bookings.services.BookingService;
import com.nerdware.roomy.features.bookings.validators.CreateBookingValidator;
import com.nerdware.roomy.features.bookings.validators.UpdateBookingValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "API for managing bookings")
public class BookingController
{
    private final BookingService bookingsService;

    public BookingController(BookingService bookingsService) {
        this.bookingsService = bookingsService;
    }

    @Operation(summary = "Get bookings overview of specific offices")
    @GetMapping("/overview")
    public ResponseEntity getBookingsOverview(@RequestParam Integer officeId)
        throws Exception
    {
        var overviewsDto = bookingsService.getBookingOverview(officeId);
        return new ResponseEntity(overviewsDto, HttpStatus.OK);
    }

    @Operation(summary = "Query by user / office")
    @GetMapping("")
    public ResponseEntity getBookings(@RequestParam Optional<Integer> officeId, @RequestParam Optional<Integer> userId)
        throws Exception
    {

        var request = new GetBookingsDto(officeId, userId);
        var bookings = bookingsService.getBookings(request);
        var bookingDtos = BookingService.toDto(bookings);

        return new ResponseEntity(bookingDtos, HttpStatus.OK);
    }

    @Operation(summary = "Get bookings overview of specific offices")
    @GetMapping("/me")
    public ResponseEntity getBookingsOfCurrentUser(Authentication authentication)
        throws Exception
    {
        var caller = (User) authentication.getPrincipal();
        var userId = Optional.of(caller.getId());

        var request = new GetBookingsDto(Optional.empty(), userId);
        var bookings = bookingsService.getBookings(request);
        var bookingDtos = BookingService.toDto(bookings);

        return new ResponseEntity(bookingDtos, HttpStatus.OK);
    }


    @Operation(summary = "Get booking by ID")
    @GetMapping("/{id}")
    public ResponseEntity getBooking(@PathVariable Integer id)
        throws Exception
    {

        var booking = bookingsService.getBookingById(id);
        var bookingDto = BookingService.toDto(booking);
        return new ResponseEntity(bookingDto, HttpStatus.OK);
    }

    @Operation(summary = "Create new booking")
    @PostMapping("")
    public ResponseEntity createBooking(@Valid @RequestBody CreateBookingDto request, BindingResult validationErrors, Authentication authentication)
        throws Exception
    {
        var validator = new CreateBookingValidator();
        validator.validate(request, validationErrors);

        if(validationErrors.hasErrors())
            throw new BadRequestException(validationErrors);

        var caller = (User) authentication.getPrincipal();

        var booking = bookingsService.createBooking(request, caller);
        var bookingDto = BookingService.toDto(booking);
        return new ResponseEntity(bookingDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete booking by ID")
    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Integer id, Authentication authentication)
        throws Exception
    {
        var caller = (User) authentication.getPrincipal();
        bookingsService.deleteBooking(id, caller);
    }

    @Operation(summary = "Update booking by ID")
    @PatchMapping("/{id}")
    public ResponseEntity updateBooking(@PathVariable Integer id, @Valid @RequestBody UpdateBookingDto request, BindingResult validationErrors, Authentication authentication)
        throws Exception
    {
        var validator = new UpdateBookingValidator();
        validator.validate(request, validationErrors);

        if(validationErrors.hasErrors())
            throw new BadRequestException(validationErrors);

        var caller = (User) authentication.getPrincipal();
        var booking = bookingsService.updateBooking(id, request, caller);

        var bookingDto = BookingService.toDto(booking);
        return new ResponseEntity(bookingDto, HttpStatus.OK);
    }
}
