package ru.practicum.shareit.exceptions;

public class BookingChangeStatusAfterApproveException extends RuntimeException {
    public BookingChangeStatusAfterApproveException(String s) {
        super(s);
    }
}
