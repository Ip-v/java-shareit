package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

/**
 * Предмет аренды mapper
 */
public class ItemMapper {
    /**
     * Item -> ItemDto
     */
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest() : null
        );
    }

    /**
     * ItemDto -> Item
     */
    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner(),
                itemDto.getRequest()
        );
    }
}
