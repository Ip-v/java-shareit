package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

/**
 * Mapper User <-> UserDto
 */
public class UserMapper {
    /**
     * Конвертиция User -> UserDto
     */
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    /**
     * Конвертиция UserDto -> User
     */
    public static User toUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }
}
