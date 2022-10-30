package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * Обработчик исключений
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {
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
     * Ошибка валидации
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationException(final ConstraintViolationException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Неверный статус
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse unsupportedStatus(final UnknownStateException e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS");
    }
}
