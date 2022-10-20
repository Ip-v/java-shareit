package ru.practicum.shareit.request.model;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;

/**
 * Mapper ItemRequest <-> ItemRequestDto
 */
public class ItemRequestMapper {
    /**
     * ItemRequest <- ItemRequestDto
     */
    public static ItemRequest toItemRequest(ItemRequestDto dto) {
        final ItemRequest request = new ItemRequest();
        request.setRequester(new User(dto.getRequesterId(), null, null));
        request.setDescription(dto.getDescription());
        return request;
    }

    /**
     * ItemRequest -> ItemRequestDto
     */
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequester().getId(),
                itemRequest.getCreated(),
                new ArrayList<>());
    }
}
