package ru.practicum.shareit.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс ошибки
 */
@AllArgsConstructor
@Getter
public class ErrorResponse {
    private final String error;
}
