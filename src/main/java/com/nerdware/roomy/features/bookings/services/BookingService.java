package com.nerdware.roomy.features.bookings.services;

import com.nerdware.roomy.domain.entities.Booking;
import com.nerdware.roomy.domain.entities.User;
import com.nerdware.roomy.domain.entities.UserRole;
import com.nerdware.roomy.domain.exceptions.BadRequestException;
import com.nerdware.roomy.domain.exceptions.ResourceNotFoundException;
import com.nerdware.roomy.domain.exceptions.UnauthorizedException;
import com.nerdware.roomy.features.bookings.dtos.requests.CreateBookingDto;
import com.nerdware.roomy.features.bookings.dtos.requests.GetBookingsDto;
import com.nerdware.roomy.features.bookings.dtos.requests.UpdateBookingDto;
import com.nerdware.roomy.features.bookings.dtos.responses.BookingDto;
import com.nerdware.roomy.features.bookings.dtos.responses.BookingOverviewDto;
import com.nerdware.roomy.features.bookings.repository.BookingRepository;
import com.nerdware.roomy.features.offices.repositories.OfficeRepository;
import com.nerdware.roomy.features.users.repositories.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final OfficeRepository officeRepository;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, OfficeRepository officeRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.officeRepository = officeRepository;
    }

    public Booking getBookingById(Integer bookingId) throws Exception
    {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    public List<Booking> getBookings(GetBookingsDto request) throws Exception
    {
        var userId = request.userId();
        var officeId = request.officeId();

        var bookings = bookingRepository.findAll(
            Specification.where(BookingRepository.hasUserId(userId)).and(BookingRepository.hasOfficeId(officeId)),
            Sort.by(Sort.Direction.DESC, "date")
        );

        return bookings;
    }

    public List<BookingOverviewDto> getBookingOverview(Integer officeId) throws Exception
    {
        var office = officeRepository.findById(officeId).orElseThrow(() -> new ResourceNotFoundException("Office not found"));
        var bookings = getBookings(new GetBookingsDto(Optional.of(officeId), Optional.empty()));

        Map<LocalDate, Long> bookingsByDate = bookings.stream()
            .collect(Collectors.groupingBy(Booking::getDate, Collectors.counting()));

        List<BookingOverviewDto> bookingOverview = new ArrayList<>();

        for (Map.Entry<LocalDate, Long> entry : bookingsByDate.entrySet()) {
            LocalDate date = entry.getKey();
            Long booked = entry.getValue();
            boolean hasFreeSpaces = booked < office.getCapacity();
            bookingOverview.add(new BookingOverviewDto(date, office.getCapacity(), booked.intValue(), hasFreeSpaces));
        }

        return bookingOverview;
    }

    public Booking createBooking(CreateBookingDto request, User caller) throws Exception
    {
        if(caller.getRole() != UserRole.Admin && caller.getId() != request.userId())
            throw new UnauthorizedException("Can not create booking for other person");

        var user = userRepository.findById(request.userId()).orElseThrow(() -> new BadRequestException("User not found"));
        var office = officeRepository.findById(request.officeId()).orElseThrow(() -> new BadRequestException("Office not found"));

        var officeId = Optional.of(request.officeId());
        var bookingDate = Optional.of(request.date());

        var bookingsCount = bookingRepository.count(
            Specification.where(BookingRepository.atDate(bookingDate)).and(BookingRepository.hasOfficeId(officeId)));

        if(bookingsCount >= office.getCapacity())
            throw new BadRequestException("Office is already completely booked");

        var booking = Booking.createBooking(office, user, request.date());
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Integer id, User caller) throws Exception
    {
        var booking = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if(caller.getRole() != UserRole.Admin && booking.getUserId() != caller.getId())
            throw new UnauthorizedException("Can not delete booking of other person");

        bookingRepository.delete(booking);
    }

    public Booking updateBooking(Integer id, UpdateBookingDto request, User caller) throws Exception
    {
        var booking = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if(caller.getRole() != UserRole.Admin && booking.getUserId() != caller.getId())
            throw new UnauthorizedException("Can not update booking of other person");


        // Old values
        var office = officeRepository.findById(booking.getOfficeId()).orElseThrow(() -> new ResourceNotFoundException("Office not found"));
        var date = booking.getDate();

        // New Values
        if(request.date() != null) {
            date = request.date();
        }

        if(request.officeId() != null) {
            office = officeRepository.findById(request.officeId()).orElseThrow(() -> new BadRequestException("Office not found"));
        }

        var officeId = Optional.of(office.getId());
        var bookingDate = Optional.of(date);

        var bookingsCount = bookingRepository.count(
            Specification.where(BookingRepository.atDate(bookingDate)).and(BookingRepository.hasOfficeId(officeId)));

        if(bookingsCount >= office.getCapacity())
            throw new BadRequestException("Office is already completely booked");

        booking.setDate(date);
        booking.setOffice(office);

        return bookingRepository.save(booking);
    }
}
