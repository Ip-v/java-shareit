package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

/**
 * Интерфейс службы бронирования
 */
public interface BookingService {

    /**
     * Создать новый
     */
    BookingInfoDto create(long userId, BookingDto bookingDto);

    /**
     * Обновить существующий
     */
    BookingDto update(long userId, Long itemId, BookingDto bookingDto);

    /**
     * Поулчить по ИД
     */
    BookingDto get(Long itemId);

    /**
     * Получить все
     */
    List<BookingInfoDto> getAll(Long userId, BookingStatus status, Pageable pageRequest);

    BookingInfoDto approveBooking(long userId, Long bookingId, boolean approved);

    List<BookingInfoDto> getOwnerBookings(long userId, BookingStatus status, Pageable pageRequest);

    BookingInfoDto getBookingById(Long userId, Long bookingId);
}
