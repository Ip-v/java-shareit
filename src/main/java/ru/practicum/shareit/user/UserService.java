package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Служба пользователей
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    /**
     * Создать нового
     */
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(repository.create(user));
    }

    /**
     * Получить по ID
     */
    public UserDto get(Long userId) {
        User user = repository.get(userId);
        return user == null ? null : UserMapper.toUserDto(user);
    }

    /**
     * Удалить по ID
     */
    public void delete(Long userId) {
        repository.delete(userId);
    }

    /**
     * Обновить пользователя
     */
    public UserDto update(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(repository.update(user));
    }

    /**
     * Получить всех пользователей
     */
    public List<UserDto> getAll() {
        return repository.getAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
