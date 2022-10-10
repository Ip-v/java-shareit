package ru.practicum.shareit.booking;

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
    BookingDto create(long userId, BookingDto bookingDto);

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
    List<BookingInfoDto> getAll(Long userId, BookingStatus status);

    /**
     * Поиск по тексту
     */
    List<BookingDto> search(Long userId, String text);

    BookingInfoDto approveBooking(long userId, Long bookingId, boolean approved);

    List<BookingInfoDto> getOwnerBookings(long userId, BookingStatus status);

    BookingInfoDto getBookingById(Long userId, Long bookingId);
}
