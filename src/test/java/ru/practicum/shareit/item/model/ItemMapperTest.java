package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemMapperTest {

    @Test
    void toItemRequestInfoDtoTest() {
        User user = new User(1L, "owner", "owner@mail.ru");
        Item item = new Item(1L, "name", "description", true, user, null);

        ItemRequestDto.ItemInfoRequestDto dto = ItemMapper.toItemRequestInfoDto(item);
        assertNotNull(dto);
        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getDescription(), dto.getDescription());
        assertEquals(item.getOwner().getId(), dto.getOwner());
    }
}