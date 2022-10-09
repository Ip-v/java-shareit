package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

/**
 * Mapper Bookings
 */
public class BookingMapper {
    /**
     * BookingDto -> Booking
     */
    public static Booking toBooking(BookingDto b) {
        return new Booking(b.getId(),
                b.getStart(),
                b.getEnd(),
                new Item(b.getItemId(), null, null, null, null, null),
                new User(b.getBookerId(), null, null),
                b.getStatus());
    }

    /**
     * Booking -> BookingInfoDto
     */
    public static BookingInfoDto toBookingInfoDto(Booking b) {
        return new BookingInfoDto(b.getId(), b.getStart(), b.getEnd(),b.getBooker(), b.getItem(), b.getStatus());
    }

    /**
     * Booking -> BookingDto
     */
    public static BookingDto toBookingDto(Booking b) {
        return new BookingDto(b.getId(),
                b.getStart(),
                b.getEnd(),
                b.getBooker().getId(),
                b.getItem().getId(),
                b.getStatus());
    }
}
