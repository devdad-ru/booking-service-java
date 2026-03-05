package com.booking.service.messaging.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Событие отклонения задания бронирования от Catalog Service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingJobDenied {
    /**
     * Идентификатор события (для трассировки)
     */
    @JsonProperty("EventId")
    private UUID eventId;

    /**
     * Распределенный идентификатор запроса (связывает с исходной командой)
     */
    @JsonProperty("RequestId")
    private UUID requestId;
}
