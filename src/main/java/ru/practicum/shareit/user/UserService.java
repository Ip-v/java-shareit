package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Служба пользователей
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository repository;

    /**
     * Создать нового
     */
    @Transactional
    public UserDto create(UserDto userDto) {
        final User user = UserMapper.toUser(userDto);
        final User save = repository.save(user);
        return UserMapper.toUserDto(save);
    }

    /**
     * Получить по ID
     */
    public UserDto get(Long userId) {
        Optional<User> userOpt = repository.findById(userId);
        if (userOpt.isPresent()) {
            return UserMapper.toUserDto(userOpt.get());
        }
        throw new NotFoundException("Пользователь не найден " + userId);
    }

    /**
     * Удалить по ID
     */
    @Transactional
    public void delete(Long userId) {
        repository.deleteById(userId);
    }

    /**
     * Обновить пользователя
     */
    @Transactional
    public UserDto update(UserDto userDto) {
        final User u = repository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден " + userDto.getId()));
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            u.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            u.setEmail(userDto.getEmail());
        }
        repository.save(u);
        return UserMapper.toUserDto(u);
    }

    /**
     * Получить всех пользователей
     */
    public List<UserDto> getAll() {
        List<UserDto> users = new ArrayList<>();
        repository.findAll().forEach(user -> users.add(UserMapper.toUserDto(user)));
        return users;
    }
}
