package ru.practicum.shareit.exceptions;

/**
 * Исключение генерируется при обнаружении дубликата
 */
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
