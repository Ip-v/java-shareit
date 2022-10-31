package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static ru.practicum.shareit.request.dto.ItemRequestDto.ItemInfoRequestDto;

class ItemRequestDtoTest {

    @Test
    void equalsTest() {
        ItemInfoRequestDto itemRequestDto1 = new ItemInfoRequestDto(1L, null, null, true,
                null, null);
        ItemInfoRequestDto itemRequestDto2 = new ItemInfoRequestDto(2L, null, null, true,
                null, null);
        ItemInfoRequestDto itemRequestDto3 = new ItemInfoRequestDto(1L, null, null, true,
                null, null);

        assertEquals(itemRequestDto1, itemRequestDto3);
        assertNotEquals(itemRequestDto1, itemRequestDto2);
        assertEquals(itemRequestDto1, itemRequestDto1);
    }

}