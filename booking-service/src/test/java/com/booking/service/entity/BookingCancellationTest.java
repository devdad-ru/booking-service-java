package com.booking.service.entity;

import com.booking.service.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class BookingCancellationTest {

    private final OffsetDateTime now = OffsetDateTime.of(2025, 1, 15, 10, 0, 0, 0, ZoneOffset.UTC);

    private Booking createBooking(BookingStatus desiredStatus) {
        Booking booking = Booking.create(1L, 1L, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 10), now);
        if (desiredStatus == BookingStatus.CONFIRMED) {
            booking.confirm();
        }
        return booking;
    }

    @Test
    void startCancellation_fromAwaitConfirmation_success() {
        Booking booking = createBooking(BookingStatus.AWAIT_CONFIRMATION);

        booking.startCancellation(now);

        assertEquals(BookingStatus.CANCELLATION_PENDING, booking.getStatus());
        assertEquals(BookingStatus.AWAIT_CONFIRMATION, booking.getPreviousStatus());
        assertEquals(now, booking.getCancellationSentAt());
    }

    @Test
    void startCancellation_fromConfirmed_success() {
        Booking booking = createBooking(BookingStatus.CONFIRMED);

        booking.startCancellation(now);

        assertEquals(BookingStatus.CANCELLATION_PENDING, booking.getStatus());
        assertEquals(BookingStatus.CONFIRMED, booking.getPreviousStatus());
        assertEquals(now, booking.getCancellationSentAt());
    }

    @Test
    void startCancellation_fromCancelled_throwsException() {
        Booking booking = createBooking(BookingStatus.AWAIT_CONFIRMATION);
        booking.cancel(LocalDate.of(2025, 1, 15));

        assertThrows(BusinessException.class, () -> booking.startCancellation(now));
    }

    @Test
    void completeCancellation_fromCancellationPending_success() {
        Booking booking = createBooking(BookingStatus.CONFIRMED);
        booking.startCancellation(now);

        booking.completeCancellation();

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertNull(booking.getPreviousStatus());
        assertNull(booking.getCancellationSentAt());
    }

    @Test
    void rollbackCancellation_restoresToAwaitConfirmation() {
        Booking booking = createBooking(BookingStatus.AWAIT_CONFIRMATION);
        booking.startCancellation(now);

        booking.rollbackCancellation();

        assertEquals(BookingStatus.AWAIT_CONFIRMATION, booking.getStatus());
        assertNull(booking.getPreviousStatus());
        assertNull(booking.getCancellationSentAt());
    }

    @Test
    void rollbackCancellation_restoresToConfirmed() {
        Booking booking = createBooking(BookingStatus.CONFIRMED);
        booking.startCancellation(now);

        booking.rollbackCancellation();

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertNull(booking.getPreviousStatus());
        assertNull(booking.getCancellationSentAt());
    }

    @Test
    void rollbackCancellation_withoutPreviousStatus_throwsException() {
        Booking booking = createBooking(BookingStatus.AWAIT_CONFIRMATION);

        assertThrows(BusinessException.class, () -> booking.rollbackCancellation());
    }
}
