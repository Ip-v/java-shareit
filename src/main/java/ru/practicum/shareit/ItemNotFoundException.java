package ru.practicum.shareit;

/**
 * Объект не найден
 */
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String s) {
        super(s);
    }
}
