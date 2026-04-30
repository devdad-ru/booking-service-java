package com.booking.service.entity;

import com.booking.service.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * JPA Entity для бронирования с инкапсулированной бизнес-логикой
 */
@Entity
@Table(name = "bookings")
@Getter
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private BookingStatus status;

    @Enumerated
    @Column(name = "previous_status")
    private BookingStatus previousStatus;

    @Column(name = "cancellation_command_sent_at")
    private OffsetDateTime cancellationCommandSentAt;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "resource_id", nullable = false)
    private Long resourceId;

    @Column(name = "booked_from", nullable = false)
    private LocalDate bookedFrom;

    @Column(name = "booked_to", nullable = false)
    private LocalDate bookedTo;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "catalog_request_id")
    private UUID catalogRequestId;

    /**
     * Factory method для создания нового бронирования с валидацией бизнес-правил
     */
    public static Booking create(Long userId, Long resourceId, LocalDate bookedFrom, LocalDate bookedTo, OffsetDateTime createdAt) {
        if (userId <= 0) {
            throw new BusinessException("Некорректный идентификатор пользователя " + userId);
        }
        if (resourceId <= 0) {
            throw new BusinessException("Некорректный идентификатор ресурса " + resourceId);
        }

        LocalDate currentDate = LocalDate.from(createdAt);
        if (!bookedFrom.isAfter(currentDate)) {
            throw new BusinessException("Дата начала бронирования должна быть больше текущей даты");
        }
        if (bookedTo.isBefore(bookedFrom)) {
            throw new BusinessException("Выбранная дата окончания бронирования раньше даты начала бронирования");
        }

        Booking booking = new Booking();
        booking.status = BookingStatus.AWAIT_CONFIRMATION;
        booking.userId = userId;
        booking.resourceId = resourceId;
        booking.bookedFrom = bookedFrom;
        booking.bookedTo = bookedTo;
        booking.createdAt = createdAt;
        return booking;
    }

    /**
     * Установить идентификатор запроса в Catalog Service
     */
    public void setCatalogRequestId(UUID catalogRequestId) {
        if (this.catalogRequestId != null) {
            throw new BusinessException("CatalogRequestId уже имеет значение: " + this.catalogRequestId);
        }
        if (catalogRequestId == null) {
            throw new BusinessException("CatalogRequestId не инициилизирован: " + catalogRequestId);
        }
        this.catalogRequestId = catalogRequestId;
    }

    /**
     * Подтвердить бронирование (переход из AwaitConfirmation в Confirmed)
     */
    public void confirm() {
        if (status != BookingStatus.AWAIT_CONFIRMATION) {
            throw new BusinessException("Статус заявки некорректен, заявка должна быть в статусе " + BookingStatus.AWAIT_CONFIRMATION);
        }
        this.status = BookingStatus.CONFIRMED;
    }

    /**
     * Отменить бронирование с учетом бизнес-правил
     */
    public void startCancellation(LocalDate currentDate, OffsetDateTime commandSentAt) {
        switch (status) {
            case AWAIT_CONFIRMATION:
                this.previousStatus = this.status;
                this.status = BookingStatus.CANCELLATION_PENDING;
                this.cancellationCommandSentAt = commandSentAt;
                break;

            case CONFIRMED:
                if (currentDate.isBefore(bookedFrom)) {
                    this.previousStatus = this.status;
                    this.status = BookingStatus.CANCELLATION_PENDING;
                    this.cancellationCommandSentAt = commandSentAt;
                } else {
                    throw new BusinessException("Невозможно отменить начавшееся бронирование");
                }
                break;

            case CANCELLATION_PENDING:
                throw new BusinessException("Отмена бронирования уже выполняется");

            case NONE:
            case CANCELLED:
            default:
                throw new BusinessException("Некорректный статус для отмены");
        }
    }

    public void completeCancellation() {
        if (status != BookingStatus.CANCELLATION_PENDING) {
            throw new BusinessException("Бронирование не находится в статусе ожидания отмены");
        }

        this.status = BookingStatus.CANCELLED;
        this.previousStatus = null;
    }

    public void rollbackCancellation() {
        if (status != BookingStatus.CANCELLATION_PENDING) {
            throw new BusinessException("Бронирование не находится в статусе ожидания отмены");
        }

        if (previousStatus == null) {
            throw new BusinessException("Невозможно откатить отмену: предыдущий статус не сохранён");
        }

        this.status = this.previousStatus;
        this.previousStatus = null;
        this.cancellationCommandSentAt = null;
    }
}