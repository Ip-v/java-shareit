package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.UnknownStateException;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Контроллер бронирования
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Validated({Create.class}) BookingDto bookingDto) {
        log.info("Создание BookingDto {}", bookingDto);
        return client.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable(name = "bookingId") Long bookingId,
                                                 @RequestParam(name = "approved") boolean approved) {
        log.info("Обновление бронирования {} пользователем {}", bookingId, approved);
        return client.approveBooking(userId, bookingId, approved);
    }

    @GetMapping(value = "/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                   @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.info("Запрос влдельца предметов бронирований ИД {}", userId);
        try {
            BookingStatus status = BookingStatus.valueOf(state);
            return client.getOwnerBookings(userId, status, from, size);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException("Unknown state: " + state);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                             @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.info("Запрос всех бронирований пользователем {}", userId);
        try {
            BookingStatus status = BookingStatus.valueOf(state);
            return client.getAll(userId, status, from, size);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException("Unknown state: " + state);
        }
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        log.info("Запроос бронирования {} пользователем {}", bookingId, userId);
        return client.getBookingById(userId, bookingId);
    }
}
