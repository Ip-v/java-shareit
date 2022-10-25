package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    private User user;
    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "owner", "owner@mail.ru");
        itemRequestDto = new ItemRequestDto(1L, "request description",
                1L, LocalDateTime.now(), null);
        itemRequest = new ItemRequest(1L, "request description", user, LocalDateTime.now());
    }

    @Test
    void toItemRequest() {
        ItemRequest request = ItemRequestMapper.toItemRequest(itemRequestDto);

        assertEquals(itemRequest.getDescription(), request.getDescription());
        assertEquals(itemRequest.getRequester().getId(), request.getRequester().getId(), "Wrong userId");
    }

    @Test
    void toItemRequestDto() {
        ItemRequestDto dto = ItemRequestMapper.toItemRequestDto(itemRequest);

        assertEquals(itemRequest.getDescription(), dto.getDescription());
        assertEquals(itemRequest.getRequester().getId(), dto.getRequesterId());
        assertEquals(itemRequest.getCreated(), dto.getCreated());
    }
}