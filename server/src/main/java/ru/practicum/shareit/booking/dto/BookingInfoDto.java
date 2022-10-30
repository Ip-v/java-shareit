package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * DTO бронирований
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookingInfoDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private User booker;
    private ItemInfo item;
    private BookingStatus status;

    @Getter
    public static class ItemInfo {
        private final Long id;
        private final String name;

        public ItemInfo(Item item) {
            this.id = item.getId();
            this.name = item.getName();
        }
    }
}
