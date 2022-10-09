package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;

/**
 * Сервис комментариев
 */
public interface CommentService {
    /**
     * Создание комментария
     */
    CommentDto createComment(CommentDto commentDto, Long itemId, Long userId);
}