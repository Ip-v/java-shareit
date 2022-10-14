package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;

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
                item.getOwner().getId(),
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
                new User(itemDto.getOwner(), null, null),
                itemDto.getRequest()
        );
    }

    /**
     * Item -> ItemInfoDto
     */
    public static ItemInfoDto itemInfoDto(Item item) {
        return new ItemInfoDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                null,
                null,
                new ArrayList<>());
    }
}
