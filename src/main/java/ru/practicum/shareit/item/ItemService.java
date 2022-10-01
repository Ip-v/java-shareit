package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * Интерфейс службы предметов аренды
 */
public interface ItemService {
    /**
     * Создать новый
     */
    ItemDto create(long userId, ItemDto itemDto);

    /**
     * Обновить существующий
     */
    ItemDto update(long userId, Long itemId, ItemDto itemDto);

    /**
     * Поулчить по ИД
     */
    ItemDto get(Long itemId);

    /**
     * Получить все
     */
    List<ItemDto> getAll(Long userId);

    /**
     * Поиск по тексту
     */
    List<ItemDto> search(Long userId, String text);
}
