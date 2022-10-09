package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

/**
 * Интерфейс службы предметов аренды
 */
public interface ItemService  {
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
    ItemInfoDto get(Long userId, Long itemId);

    /**
     * Получить все
     */
    List<ItemInfoDto> getAll(Long userId);

    /**
     * Поиск по тексту
     */
    List<ItemDto> search(Long userId, String text);

    /**
     * Проверка доступности предмета для бронирования
     */
    boolean isItemAvailable(Long itemId);
}
