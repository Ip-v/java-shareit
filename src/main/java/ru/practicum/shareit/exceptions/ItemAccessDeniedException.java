package ru.practicum.shareit.exceptions;

/**
 * Исключение при отсутствии прав доступа
 */
public class ItemAccessDeniedException extends RuntimeException {
    public ItemAccessDeniedException(String s) {
        super(s);
    }
}
