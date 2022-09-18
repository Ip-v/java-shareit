package ru.practicum.shareit.user;

import lombok.*;

/**
 * Класс пользователей
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
}
