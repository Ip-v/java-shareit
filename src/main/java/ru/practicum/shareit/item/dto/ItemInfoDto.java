package ru.practicum.shareit.item.dto;


import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ItemInfoDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private ItemWithBookingDto lastBooking;
    private ItemWithBookingDto nextBooking;
    private List<CommentDto> comments;
}


