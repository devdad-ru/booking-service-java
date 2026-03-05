package com.booking.service.messaging.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Команда на отмену задания бронирования в Catalog Service по requestId.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelBookingJobByRequestIdRequest {
    /**
     * Идентификатор события (для трассировки)
     */
    @JsonProperty("EventId")
    private UUID eventId;

    /**
     * Распределенный идентификатор запроса (для поиска задания)
     */
    @JsonProperty("RequestId")
    private UUID requestId;
}
