package ru.practicum.shareit.exceptions;

/**
 * Объект не найден
 */
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String s) {
        super(s);
    }
}
