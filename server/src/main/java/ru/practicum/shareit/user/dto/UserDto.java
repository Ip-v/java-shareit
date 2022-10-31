package ru.practicum.shareit.user.dto;

import lombok.*;

import java.util.Objects;

/**
 * DTO Пользователей
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) && email.equals(userDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
