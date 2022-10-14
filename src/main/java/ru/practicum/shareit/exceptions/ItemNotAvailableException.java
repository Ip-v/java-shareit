package ru.practicum.shareit.exceptions;
/**
 * Нет доступа
 */
public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(String s) {
        super(s);
    }
}
