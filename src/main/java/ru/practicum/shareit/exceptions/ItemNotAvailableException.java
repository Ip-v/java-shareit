package ru.practicum.shareit.exceptions;

public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(String s) {
        super(s);
    }
}
