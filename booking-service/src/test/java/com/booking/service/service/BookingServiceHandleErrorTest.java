package com.booking.service.service;

import com.booking.service.config.CurrentDateTimeProvider;
import com.booking.service.entity.Booking;
import com.booking.service.entity.BookingStatus;
import com.booking.service.messaging.listener.BookingEventPublisher;
import com.booking.service.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceHandleErrorTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingEventPublisher bookingEventPublisher;

    @Mock
    private CurrentDateTimeProvider dateTimeProvider;

    @InjectMocks
    private BookingService bookingService;

    private final OffsetDateTime now = OffsetDateTime.of(2025, 1, 15, 10, 0, 0, 0, ZoneOffset.UTC);

    @Test
    void handleError_rollbacksCancellation() {
        UUID requestId = UUID.randomUUID();
        Booking booking = Booking.create(1L, 1L, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 10), now);
        booking.setCatalogRequestId(requestId);
        booking.confirm();
        booking.startCancellation(now);

        when(bookingRepository.findByCatalogRequestId(requestId)).thenReturn(Optional.of(booking));

        bookingService.handleError(requestId);

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void handleError_bookingNotFound_logsWarning() {
        UUID requestId = UUID.randomUUID();
        when(bookingRepository.findByCatalogRequestId(requestId)).thenReturn(Optional.empty());

        bookingService.handleError(requestId);

        verify(bookingRepository, never()).save(any());
    }
}
