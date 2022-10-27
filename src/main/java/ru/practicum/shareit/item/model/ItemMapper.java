package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    /**
     * Item -> ItemRequestDto.ItemInfoRequestDto
     */
    public static ItemRequestDto.ItemInfoRequestDto toItemRequestInfoDto(Item item) {
        return new ItemRequestDto.ItemInfoRequestDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                item.getOwner().getId(),
                item.getRequest() != null ? item.getRequest().getId() : null
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
                null
        );
    }

    /**
     * Item -> ItemInfoDto
     */
    public static ItemInfoDto toItemInfoDto(Item item) {
        return new ItemInfoDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                null,
                null,
                new ArrayList<>());
    }

    public static List<ItemRequestDto.ItemInfoRequestDto> toItemDtoList(List<Item> items) {
        return items.stream().map(ItemMapper::toItemRequestInfoDto).collect(Collectors.toList());
    }
}
