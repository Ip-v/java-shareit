package ru.practicum.shareit.exceptions;

/**
 * Объект не найден
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String s) {
        super(s);
    }
}
