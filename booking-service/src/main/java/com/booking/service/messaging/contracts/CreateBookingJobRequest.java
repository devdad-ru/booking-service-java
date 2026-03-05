package com.booking.service.messaging.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Команда на создание задания бронирования в Catalog Service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingJobRequest {
    /**
     * Идентификатор события (для трассировки)
     */
    @JsonProperty("EventId")
    private UUID eventId;

    /**
     * Распределенный идентификатор запроса (связывает команду и ответные события)
     */
    @JsonProperty("RequestId")
    private UUID requestId;

    /**
     * Идентификатор ресурса для бронирования
     */
    @JsonProperty("ResourceId")
    private Long resourceId;

    /**
     * Дата начала бронирования
     */
    @JsonProperty("StartDate")
    private LocalDate startDate;

    /**
     * Дата окончания бронирования
     */
    @JsonProperty("EndDate")
    private LocalDate endDate;
}
