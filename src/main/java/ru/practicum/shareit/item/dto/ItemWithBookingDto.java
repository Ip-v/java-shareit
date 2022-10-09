package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.booking.model.Booking;

@ToString
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ItemWithBookingDto {
    private Long id;
    private Long bookerId;

    public ItemWithBookingDto(Booking booking) {
        this.id = booking.getId();
        this.bookerId = booking.getBooker().getId();
    }
}