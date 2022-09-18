package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

/**
 * Предмет аренды
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean isAvailable;
    private Long owner;
    private ItemRequest request;
}
