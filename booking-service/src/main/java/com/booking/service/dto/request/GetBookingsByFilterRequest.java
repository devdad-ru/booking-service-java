package com.booking.service.dto.request;

import com.booking.service.entity.BookingStatus;

/**
 * Запрос на получение списка бронирований с фильтрацией и пагинацией
 */
public record GetBookingsByFilterRequest(
        Long userId,
        Long resourceId,
        BookingStatus status,
        Integer pageNumber,
        Integer pageSize) {

    public GetBookingsByFilterRequest {
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null) {
            pageSize = 25;
        }
    }
}
