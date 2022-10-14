package ru.practicum.shareit.exceptions;
/**
 * Неверный статус
 */
public class UnknownStateException extends RuntimeException {
    public UnknownStateException(String s) {
        super(s);
    }
}
