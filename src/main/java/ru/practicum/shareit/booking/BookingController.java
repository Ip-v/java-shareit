package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.UnknownStateException;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Контроллер бронирования
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingInfoDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                             @RequestBody @Validated({Create.class}) BookingDto bookingDto) {
        log.info("Создание BookingDto {}", bookingDto);
        return service.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingInfoDto approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable(name = "bookingId") Long bookingId,
                                         @RequestParam(name = "approved") boolean approved) {
        log.info("Обновление бронирования {} пользователем {}", bookingId, approved);
        return service.approveBooking(userId, bookingId, approved);
    }

    @GetMapping(value = "/owner")
    public List<BookingInfoDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                 @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.info("Запрос влдельца предметов бронирований ИД {}", userId);
        try {
            BookingStatus status = BookingStatus.valueOf(state);
            int page = from / size;
            Pageable pageRequest = PageRequest.of(page, size, Sort.by("start").descending());
            return service.getOwnerBookings(userId, status, pageRequest);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException("Unknown state: " + state);
        }
    }

    @GetMapping
    public List<BookingInfoDto> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                           @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.info("Запрос всех бронирований пользователем {}", userId);
        try {
            BookingStatus status = BookingStatus.valueOf(state);
            int page = from / size;
            Pageable pageRequest = PageRequest.of(page, size, Sort.by("start").descending());
            return service.getAll(userId, status, pageRequest);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException("Unknown state: " + state);
        }
    }

    @GetMapping("/{bookingId}")
    public BookingInfoDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        log.info("Запроос бронирования {} пользователем {}", bookingId, userId);
        return service.getBookingById(userId, bookingId);
    }
}
