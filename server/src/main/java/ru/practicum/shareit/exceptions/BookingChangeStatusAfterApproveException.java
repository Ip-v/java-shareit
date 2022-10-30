package ru.practicum.shareit.exceptions;

/**
 * Исключение генерируется при обнаружении правил бронирования
 */
public class BookingChangeStatusAfterApproveException extends RuntimeException {
    public BookingChangeStatusAfterApproveException(String s) {
        super(s);
    }
}
