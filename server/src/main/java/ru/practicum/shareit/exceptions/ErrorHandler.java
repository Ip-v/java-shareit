package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Обработчик исключений
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    /**
     * Не найдено
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(final NotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Нет доступа
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse accessDenied(final ItemAccessDeniedException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Нет доступа
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse accessDenied(final CommentAccessDeniedException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Ошибка валидации
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse validationException(final DataIntegrityViolationException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Нет доступа
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse accessDenied(final BookingChangeStatusAfterApproveException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Нет доступа
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse bookingNotAvailable(final ItemNotAvailableException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
