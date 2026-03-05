package com.booking.service.service.mapper;

import com.booking.service.dto.response.BookingResponse;
import com.booking.service.entity.Booking;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper для преобразования Entity в DTO
 */
@Component
public class BookingMapper {

    /**
     * Маппинг Booking Entity → BookingResponse DTO
     */
    public BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getStatus(),
                booking.getUserId(),
                booking.getResourceId(),
                booking.getBookedFrom(),
                booking.getBookedTo(),
                booking.getCreatedAt()
        );
    }

    /**
     * Маппинг списка Booking Entity → список BookingResponse DTO
     */
    public List<BookingResponse> toResponseList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toResponse)
                .toList();
    }
}
