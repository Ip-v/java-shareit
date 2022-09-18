package ru.practicum.shareit;

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
