package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * DTO комментария
 */
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


