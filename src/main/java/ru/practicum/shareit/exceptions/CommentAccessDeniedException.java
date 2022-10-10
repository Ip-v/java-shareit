package ru.practicum.shareit.exceptions;

/**
 * Исключение генерируется при обнаружении правил комментирования
 */
public class CommentAccessDeniedException extends RuntimeException {
    public CommentAccessDeniedException(String s) {
        super(s);
    }
}
