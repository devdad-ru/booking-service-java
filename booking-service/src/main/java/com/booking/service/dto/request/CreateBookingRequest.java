package com.booking.service.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Запрос на создание бронирования
 */
public record CreateBookingRequest(@NotNull Long userId, @NotNull Long resourceId, @NotNull LocalDate bookedFrom,
                                   @NotNull LocalDate bookedTo) {
}
