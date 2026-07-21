package com.booking.service.dto.response;

import java.util.List;

public record BookingStatsResponse(
        long totalBookings,
        List<BookingStatusStats> byStatus,
        List<ResourceStats> topResources
) {
}
