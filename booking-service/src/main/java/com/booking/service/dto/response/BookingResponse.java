package com.booking.service.dto.response;

import com.booking.service.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * DTO для передачи данных о бронировании
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private BookingStatus bookingStatus;
    private Long userId;
    private Long resourceId;
    private LocalDate bookedFrom;
    private LocalDate bookedTo;
    private OffsetDateTime createdAt;
}
