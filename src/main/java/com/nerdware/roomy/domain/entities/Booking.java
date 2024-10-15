package com.nerdware.roomy.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Table(name = "bookings")
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Getter
    private Integer id;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(nullable = false)
    @Getter
    @Setter
    private LocalDate date;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    private User user;

    @ManyToOne()
    @JoinColumn(name = "office_id", nullable = false)
    @Getter
    @Setter
    private Office office;

    public Integer getOfficeId() {
        return getOffice().getId();
    }

    public Integer getUserId() {
        return getUser().getId();
    }

    public static Booking createBooking(Office office, User user, LocalDate date) {
        var booking = new Booking();
        booking.setDate(date == null ? LocalDate.now() : date);
        booking.setUser(user);
        booking.setOffice(office);
        return booking;
    }
}
