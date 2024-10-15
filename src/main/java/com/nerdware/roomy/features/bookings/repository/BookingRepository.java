package com.nerdware.roomy.features.bookings.repository;

import com.nerdware.roomy.domain.entities.Booking;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Integer>, JpaSpecificationExecutor<Booking>
{

    public static Specification<Booking> hasUserId(Optional<Integer> userId) {
        return (booking, cq, cb) -> userId.map(id -> cb.equal(booking.get("user").get("id"), id)).orElseGet(() -> cb.isTrue(cb.literal(true)));
    }

    public static Specification<Booking> hasOfficeId(Optional<Integer> officeId) {
        return (booking, cq, cb) -> officeId.map(id -> cb.equal(booking.get("office").get("id"), id)).orElseGet(() -> cb.isTrue(cb.literal(true)));
    }

    public static Specification<Booking> atDate(Optional<LocalDate> bookingDate) {
        return (booking, cq, cb) -> bookingDate.map(date -> cb.equal(booking.get("date"), date)).orElseGet(() -> cb.isTrue(cb.literal(true)));
    }
}
