package com.booking.service.dto.response;

import com.booking.service.entity.BookingStatus;

public record BookingStatusStats(
        BookingStatus status,
        long count
) {
}